package com.lingyun.query.criteria;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.annotation.AnnotatedElementUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingyun.query.annotation.In;
import com.lingyun.query.annotation.LikeLeft;
import com.lingyun.query.annotation.OmitValueClause;
import com.lingyun.query.annotation.QueryAnnotation;
import com.lingyun.query.annotation.Scope;
import com.lingyun.query.condition.Between;
import com.lingyun.query.condition.QueryCondition;
import com.lingyun.query.condition.QueryConditionUtils;

/**
 * 查询请求标准化解析器（猫猫核心魔法类）.
 * <p>
 * 这是整个查询 DSL 框架中最关键的类——它接收原始的 JSON 请求字符串和目标实体类型，
 * 通过 Jackson 反序列化 + Spring 注解扫描的双引擎驱动，自动将请求翻译为标准化的
 * {@link CriteriaQuery}。所有猫猫的魔法都在这里发生喵~
 * </p>
 *
 * <h3>解析流程（两阶段扫描）</h3>
 * <ol>
 *   <li><b>阶段一：无值字段扫描</b> — 遍历目标类型的所有声明字段，找出标注了
 *       {@link OmitValueClause} 的字段（如 {@code @Asc}、{@code @Desc}），
 *       以 {@code null} 为值生成对应的 {@link QueryCondition}。
 *       这些条件不需要请求方传入任何参数，语义自包含喵。</li>
 *   <li><b>阶段二：有值字段扫描</b> — 遍历 JSON 树中的每个字段，根据字段名
 *       在目标类型上反射查找 {@link java.lang.reflect.Field}，再检查其上的
 *       {@link QueryAnnotation} 注解来决定生成哪种条件：
 *       <ul>
 *         <li>无注解 → 默认生成 {@code EqCondition}（等值匹配）</li>
 *         <li>有注解 → 委托给 {@link QueryConditionUtils#getCondition} 按注解类型生成</li>
 *       </ul>
 *   </li>
 * </ol>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * // 定义查询实体
 * public static class UserQuery implements Serializable {
 *     @LikeLeft
 *     private String name;
 *     @Scope
 *     private ScopeCondition<Integer> age;
 *     @In
 *     private Long[] ids;
 *     @Asc
 *     @OmitValueClause  // Asc 本身已标注 @OmitValueClause，这里是展示用
 *     private String createTime;
 * }
 *
 * // Controller 中使用
 * CriteriaQueryParser parser = new CriteriaQueryParser();
 * CriteriaQuery<UserQuery> query = parser.parse(
 *     "{\"name\":\"zhang\", \"age\":{\"le\":30, \"gt\":18}, \"ids\":[1,2,3]}",
 *     UserQuery.class
 * );
 *
 * // 结果:
 * // query.getRaw()       → 完整反序列化的 UserQuery 对象
 * // query.getConditions() → [
 * //     LikeCondition(fieldName="name", value="zhang", option=LIKE_LEFT),
 * //     ScopeCondition(fieldName="age", lowerLimit=18, upperLimit=30, ...),
 * //     InCondition(fieldName="ids", elements=[1, 2, 3]),
 * //     OrderCondition(fieldName="createTime", option=ASC)  ← 无需传值也生成了!
 * // ]
 * }</pre>
 *
 * <p>
 * 注意：解析失败时会抛出 {@link QueryRequestNormalizationFailedException}，
 * 调用方应在 Controller 层统一捕获并交给 RSM 框架返回友好错误响应喵~
 * </p>
 *
 * @see CriteriaQuery
 * @see QueryConditionUtils
 * @see QueryAnnotation
 * @see OmitValueClause
 * @see QueryRequestNormalizationFailedException
 */
public class CriteriaQueryParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将 JSON 请求字符串解析为 {@link CriteriaQuery} 标准化查询对象.
     * <p>
     * 这是解析器的唯一对外入口。内部执行两阶段扫描（无值字段 → 有值字段），
     * 最终产出包含原始对象和标准化条件集合的 {@link OrdinaryCriteriaQuery} 实例。
     * </p>
     *
     * <h3>阶段一：无值字段</h3>
     * 遍历 {@code type} 所有声明字段，找出标注了 {@link OmitValueClause} 的字段，
     * 以 {@code null} 作为值调用 {@link QueryConditionUtils#getCondition} 生成条件。
     * <p>
     * 典型应用：{@code @Asc}、{@code @Desc} — 排序方向由注解自身决定，无需请求传值。
     * </p>
     *
     * <h3>阶段二：有值字段</h3>
     * 遍历 JSON 节点的每个 entry，用 Jackson 的 {@link ObjectMapper#convertValue} 将
     * JSON 值转换为字段声明的 Java 类型，然后：
     * <ul>
     *   <li>若字段有 {@link QueryAnnotation} → 按注解类型生成对应条件</li>
     *   <li>若字段无注解 → 默认生成 {@link com.lingyun.query.condition.EqCondition}（等值匹配）</li>
     * </ul>
     * 已在阶段一中处理过的字段会被跳过（避免重复生成）。
     *
     * <h3>异常处理</h3>
     * JSON 解析失败时（格式错误、类型不匹配等），会抛出
     * {@link QueryRequestNormalizationFailedException} 包装原始异常，
     * 供上层统一处理喵。
     *
     * @param jsonString 请求端发来的 JSON 字符串，不可为 {@code null}
     * @param type       查询条件实体类的 {@link Class} 对象，作为反序列化 + 注解扫描的目标
     * @param <T>        查询请求实体类型，必须实现 {@link Serializable}
     * @return 包含原始对象和标准化条件集合的 {@link CriteriaQuery}
     * @throws QueryRequestNormalizationFailedException JSON 反序列化失败时抛出
     */
    public <T extends Serializable> CriteriaQuery<T> parse(
            String jsonString, Class<T> type) {
        T raw = null;
        CriteriaQuery<T> query;
        try {
            JsonNode tree = objectMapper.readTree(jsonString);
            raw = objectMapper.readValue(jsonString, type);
            query = new OrdinaryCriteriaQuery<T>(raw);
            List<QueryCondition> conditions = new ArrayList<>(tree.size());
            Field[] declaredFields = type.getDeclaredFields();
            // 寻找无需传入参数的注解
            Map<String, Field> fieldMap = Arrays.stream(declaredFields)
                    .filter(field -> AnnotatedElementUtils.hasAnnotation(field, OmitValueClause.class))
                    .collect(Collectors.toMap(Field::getName, f -> f));
            fieldMap.forEach((fieldName, field) -> {
                Set<QueryAnnotation> queryAnnotations = AnnotatedElementUtils
                        .findAllMergedAnnotations(field, QueryAnnotation.class);
                if (!queryAnnotations.isEmpty()) {
                    conditions.addAll(queryAnnotations.stream()
                            .map(ann -> QueryConditionUtils.getCondition(ann, fieldName, null))
                            .filter(Objects::nonNull)
                            .toList());
                }
            });

            // 寻找需要参数的注解
            tree.forEachEntry((fieldName, value) -> {
                try {
                    if (fieldMap.containsKey(fieldName)) {
                        return;
                    }
                    Field declaredField = type.getDeclaredField(fieldName);
                    Set<QueryAnnotation> queryAnnotations = AnnotatedElementUtils
                            .findAllMergedAnnotations(declaredField, QueryAnnotation.class);
                    Class<?> fieldType = declaredField.getType();
                    Object v = objectMapper.convertValue(value, fieldType);
                    if (queryAnnotations.isEmpty()) {
                        conditions.add(QueryConditionUtils.createEqCondition(fieldName, v));
                    } else {
                        conditions.addAll(queryAnnotations.stream()
                                .map(ann -> QueryConditionUtils.getCondition(ann, fieldName, v))
                                .filter(Objects::nonNull)
                                .toList());
                    }
                } catch (NoSuchFieldException | SecurityException e) {
                }
            });
            query.setConditions(conditions);
            return query;
        } catch (JsonProcessingException e) {
            throw new QueryRequestNormalizationFailedException("[查询条件反序列化失败]" ,e);
        }
    }

    /**
     * 演示用入口（仅开发调试，生产环境请勿使用）.
     * <p>
     * 展示完整的解析流程：一条 JSON 同时包含字符串（模糊查询）、嵌套对象（范围查询）、
     * 数组（IN 查询），且隐含了 {@code @Asc} 注解的无值字段（{@code createTime} 不会出现在
     * 请求 JSON 中，但仍能被解析器识别喵）。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        CriteriaQueryParser parser = new CriteriaQueryParser();
        CriteriaQuery<Demo> x = parser.parse("{ \"name\": \"zhang\", \"age\": [1 ,10], \"ids\": [1,2,3]}", Demo.class);
        System.out.println(x.getRaw());
        System.out.println(x.getConditions());
    }

    /**
     * 演示用内部查询实体（私有 static 类，仅供 {@link #main} 使用）.
     * <p>
     * 包含三种典型查询字段：
     * <ul>
     *   <li>{@code name} — {@link LikeLeft} 左模糊查询</li>
     *   <li>{@code age} — {@link Scope} 范围查询（BETWEEN 区间）</li>
     *   <li>{@code ids} — {@link In} 集合包含查询</li>
     * </ul>
     */
    static class Demo implements Serializable {
        @LikeLeft
        private String name;
        @Scope
        private Between<Integer> age;
        @In
        private int[] ids;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public Between<Integer> getAge() {
            return age;
        }

        public void setAge(Between<Integer> age) {
            this.age = age;
        }
        public int[] getIds() {
            return ids;
        }

        public void setIds(int[] ids) {
            this.ids = ids;
        }
    }
}
