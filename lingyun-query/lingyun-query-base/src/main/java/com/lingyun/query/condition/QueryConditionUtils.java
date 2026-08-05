package com.lingyun.query.condition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.lingyun.query.annotation.Asc;
import com.lingyun.query.annotation.Desc;
import com.lingyun.query.annotation.LikeLeft;
import com.lingyun.query.annotation.LikeRight;
import com.lingyun.query.annotation.QueryAnnotation;

/**
 * 查询条件构建工具类（猫猫工厂）.
 * <p>
 * 提供从注解元数据（{@link QueryAnnotation}）和字段值快速构建 {@link QueryCondition} 实例的静态工厂方法。
 * 是 {@link com.lingyun.query.criteria.CriteriaQueryParser} 的核心依赖——解析器负责扫描字段和注解，
 * 而本工具类负责把注解 + 字段名 + 值三要素捏合成具体的条件对象喵。
 * </p>
 *
 * <h3>核心入口</h3>
 * <ul>
 * <li>{@link #createEqCondition(String, Object)} — 创建等值条件（默认行为，无注解时使用）</li>
 * <li>{@link #getCondition(QueryAnnotation, String, Object)} —
 * 根据注解元数据创建对应的条件（处理所有已知类型，通过直接 {@code new} 实例化）</li>
 * <li>{@link #arrayToList(Object)} — 将数组转为 List（IN/NOT IN 条件使用）</li>
 * </ul>
 *
 * <h3>条件类型调度逻辑</h3>
 * {@link #getCondition} 内部根据 {@code conditionType} 逐类匹配并直接 {@code new} 实例化：
 * <ul>
 * <li>{@link ScopeCondition} — 值本身就是条件对象，仅补填 fieldName</li>
 * <li>{@link IsNullCondition} — 无值条件，直接 new IsNullCondition(fieldName)</li>
 * <li>{@link IsNotNullCondition} — 无值条件，直接 new
 * IsNotNullCondition(fieldName)</li>
 * <li>{@link OrderCondition} — 根据原始注解区分 ASC / DESC</li>
 * <li>{@link LikeCondition} — 根据原始注解区分 LIKE_LEFT / LIKE_RIGHT / LIKE</li>
 * <li>{@link InCondition} — 将数组或 Collection 注入 elements</li>
 * <li>{@link NotInCondition} — 将数组或 Collection 注入 elements（NOT IN 语义）</li>
 * <li>其他 — 返回 {@code null}，不做特殊处理</li>
 * </ul>
 *
 * <p>
 * 工具类为 {@code final} 且构造器私有（不可实例化），所有方法均为静态——纯粹的猫猫函数式风格喵~
 * </p>
 *
 * @see QueryCondition
 * @see QueryAnnotation
 * @see com.lingyun.query.criteria.CriteriaQueryParser
 */
public final class QueryConditionUtils {

    private QueryConditionUtils() {
        // 工具类，禁止实例化喵
    }

    /**
     * 创建一个等值查询条件（{@code fieldName = value}）.
     * <p>
     * 这是无特定查询注解时的默认行为——框架假定未被标注的字段应做精确等值匹配。
     * </p>
     *
     * @param fieldName 实体字段名
     * @param value     查询值（可为 {@code null}）
     * @return 包装好的 {@link EqCondition} 实例
     */
    public static EqCondition<Object> createEqCondition(String fieldName, Object value) {
        EqCondition<Object> eqCondition = new EqCondition<>();
        eqCondition.setFieldName(fieldName);
        eqCondition.setValue(value);
        return eqCondition;
    }

    /**
     * 根据 {@link QueryAnnotation} 元数据创建对应的 {@link QueryCondition}.
     * <p>
     * 这是整个查询 DSL 条件构建的核心调度方法。它读取 {@code queryAnnotation} 的
     * {@link QueryAnnotation#conditionType()} 来决定实例化哪种条件类，
     * 再根据 {@link QueryAnnotation#rawAnnotation()} 和 {@code value} 的实际类型
     * 来填充条件对象的细节（排序方向、模糊匹配模式、IN/NOT IN 元素集合等）。
     * 所有实例化均采用直接 {@code new} 调用，不使用反射——猫猫喜欢直接了当喵~
     * </p>
     *
     * <h3>调度规则一览</h3>
     * <table>
     * <tr>
     * <th>条件类型</th>
     * <th>value 要求</th>
     * <th>额外逻辑</th>
     * </tr>
     * <tr>
     * <td>{@link ScopeCondition}</td>
     * <td>value 本身就是 {@code ScopeCondition}</td>
     * <td>仅回填 fieldName</td>
     * </tr>
     * <tr>
     * <td>{@link IsNullCondition}</td>
     * <td>忽略（{@code IS NULL} 无值语义）</td>
     * <td>直接 {@code new IsNullCondition(fieldName)}</td>
     * </tr>
     * <tr>
     * <td>{@link IsNotNullCondition}</td>
     * <td>忽略（{@code IS NOT NULL} 无值语义）</td>
     * <td>直接 {@code new IsNotNullCondition(fieldName)}</td>
     * </tr>
     * <tr>
     * <td>{@link OrderCondition}</td>
     * <td>可为 {@code null}（ASC/DESC 由 rawAnnotation 决定）</td>
     * <td>区分 ASC / DESC / 自定义</td>
     * </tr>
     * <tr>
     * <td>{@link LikeCondition}</td>
     * <td>任意值</td>
     * <td>区分 LIKE_LEFT / LIKE_RIGHT / LIKE</td>
     * </tr>
     * <tr>
     * <td>{@link InCondition}</td>
     * <td>Collection 或数组</td>
     * <td>数组自动转为 List</td>
     * </tr>
     * <tr>
     * <td>{@link NotInCondition}</td>
     * <td>Collection 或数组</td>
     * <td>数组自动转为 List（NOT IN 语义）</td>
     * </tr>
     * <tr>
     * <td>其他</td>
     * <td>—</td>
     * <td>返回 {@code null}</td>
     * </tr>
     * </table>
     *
     * @param queryAnnotation 元注解，包含条件类型和原始注解的映射信息
     * @param fieldName       实体字段名
     * @param value           从请求 JSON 中解析出来的字段值（类型依条件而定）
     * @return 构建好的 {@link QueryCondition} 实例；构建失败或无法识别类型时返回 {@code null}
     */
    public static QueryCondition getCondition(QueryAnnotation queryAnnotation, String fieldName, Object value) {
        if (!queryAnnotation.name().isBlank()) {
            fieldName = queryAnnotation.name();
        }

        Class<? extends QueryCondition> conditionType = queryAnnotation.conditionType();

        if (EqCondition.class.equals(conditionType)) {
            EqCondition<Object> eqCondition = new EqCondition<>();
            eqCondition.setFieldName(fieldName);
            eqCondition.setValue(value);
            return eqCondition;
        }

        if (ScopeCondition.class.equals(conditionType)) { // Scope Ge Gt Le Lt Between
            if (value instanceof ScopeCondition condition) {
                condition.setFieldName(fieldName);
                return condition;
            }
        }
        if (IsNullCondition.class.equals(conditionType)) { // IsNull
            return new IsNullCondition(fieldName);
        }

        if (IsNotNullCondition.class.equals(conditionType)) { // IsNotNull
            return new IsNotNullCondition(fieldName);
        }

        if (OrderCondition.class.equals(conditionType)) { // Order Asc Desc
            OrderCondition orderCondition = new OrderCondition();
            Class<? extends Annotation> rawAnnotation = queryAnnotation.rawAnnotation();
            if (Asc.class.equals(rawAnnotation)) {
                orderCondition.setOption(OrderOption.ASC);
            } else if (Desc.class.equals(rawAnnotation)) {
                orderCondition.setOption(OrderOption.DESC);
            } else {
                if (value != null) {
                    if (value instanceof OrderOption option) {
                        orderCondition.setOption(option);
                    } else if (value instanceof OrderCondition condition) {
                        orderCondition.setOption(condition.getOption());
                    } else if (value instanceof String strValue) {
                        OrderOption option = switch (strValue.toUpperCase()) {
                            case "ASC" -> OrderOption.ASC;
                            case "DESC" -> OrderOption.DESC;
                            default -> null;
                        };
                        if (option == null) {
                            return null;
                        }
                        orderCondition.setOption(option);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            orderCondition.setFieldName(fieldName);
            return orderCondition;
        }

        if (LikeCondition.class.equals(conditionType)) { // Like LikeLeft LikeRight
            LikeCondition<Object> likeCondition = new LikeCondition<>();
            Class<? extends Annotation> rawAnnotation = queryAnnotation.rawAnnotation();
            likeCondition.setValue(value);
            if (LikeLeft.class.equals(rawAnnotation)) {
                likeCondition.setOption(LikeOption.LIKE_LEFT);
            } else if (LikeRight.class.equals(rawAnnotation)) {
                likeCondition.setOption(LikeOption.LIKE_RIGHT);
            } else {
                likeCondition.setOption(LikeOption.LIKE);
            }
            likeCondition.setFieldName(fieldName);
            return likeCondition;
        }

        if (InCondition.class.equals(conditionType)) { // In
            InCondition<Object> inCondition = new InCondition<>();
            if (value instanceof Collection coll) {
                inCondition.setElements(coll);
            } else if (value != null && value.getClass().isArray()) {
                inCondition.setElements(arrayToList(value));
            } else {
                return null;
            }
            inCondition.setFieldName(fieldName);
            return inCondition;
        }

        if (NotInCondition.class.equals(conditionType)) { // NotIn
            NotInCondition<Object> notInCondition = new NotInCondition<>();
            if (value instanceof Collection coll) {
                notInCondition.setElements(coll);
            } else if (value != null && value.getClass().isArray()) {
                notInCondition.setElements(arrayToList(value));
            } else {
                return null;
            }
            notInCondition.setFieldName(fieldName);
            return notInCondition;
        }
        return null;
    }

    /**
     * 将任意类型数组转换为 {@link List}{@code <Object>}.
     * <p>
     * 使用反射 API（{@link Array#getLength} / {@link Array#get}）处理，
     * 因此无论是 {@code int[]}、{@code String[]} 还是其他基本类型数组都能正确转换喵。
     * 通过 {@link Array#get} 读取元素时，基本类型会自动装箱为包装类型。
     * </p>
     *
     * @param obj 任意数组对象（如 {@code int[]}、{@code String[]}），可为 {@code null}
     * @return 转换后的 List；若 {@code obj} 为 {@code null} 则返回 {@code null}
     */
    public static List<Object> arrayToList(Object obj) {
        if (obj == null) {
            return null;
        }
        int length = Array.getLength(obj); // 反射获取数组长度（基本类型也支持）
        List<Object> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(Array.get(obj, i)); // 反射获取每个元素（自动装箱）
        }
        return list;
    }
}
