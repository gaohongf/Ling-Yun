package com.lingyun.query.annotation;

import java.lang.annotation.*;

import com.lingyun.query.condition.QueryCondition;

/**
 * 查询注解到查询条件的映射元注解.
 * <p>
 * 这是查询 DSL 中最核心的元注解——它声明了一个业务查询注解（如 {@link Like}、{@link Scope}、{@link In}）
 * 与对应的 {@link QueryCondition} 实现类之间的映射关系。
 * 每一个具体查询注解都必须标注 {@code @QueryAnnotation}，否则框架无法识别它的语义喵！
 * </p>
 *
 * <h3>映射关系</h3>
 * <ul>
 *   <li>{@link #conditionType()} — 该注解对应的 {@link QueryCondition} 子类，
 *       框架通过反射实例化它并填充字段信息</li>
 *   <li>{@link #rawAnnotation()} — 原始注解自身（用于反向查找，
 *       例如从注解类反向定位到元数据）</li>
 * </ul>
 *
 * <h3>如何声明一个新的查询注解</h3>
 * <pre>{@code
 * // 1. 定义注解
 * @QueryAnnotation(conditionType = MyCustomCondition.class, rawAnnotation = MyCustom.class)
 * @Target({ElementType.FIELD, ElementType.PARAMETER})
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface MyCustom { }
 *
 * // 2. 实现对应的 QueryCondition 子类
 * public class MyCustomCondition extends AbstractQueryCondition { ... }
 *
 * // 3. 在 QueryConditionUtils.getCondition() 中增加该类型的处理分支
 * }</pre>
 * <p>
 * 三步走完，你的自定义查询注解就能自动被 {@link com.lingyun.query.criteria.CriteriaQueryParser} 识别了喵~
 * </p>
 *
 * @see QueryCondition
 * @see com.lingyun.query.condition.QueryConditionUtils
 * @see com.lingyun.query.criteria.CriteriaQueryParser
 */
@Inherited
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryAnnotation {

    /**
     * 该注解映射到的 {@link QueryCondition} 子类.
     * <p>
     * 框架通过此属性获知该查询注解应该生成哪种条件对象。
     * 指定的类必须拥有无参构造函数，因为框架通过反射实例化它。
     * </p>
     *
     * @return {@link QueryCondition} 的具体实现类
     */
    Class<? extends QueryCondition> conditionType();

    /**
     * 原始注解自身的 {@link Class} 对象.
     * <p>
     * 用于从条件类型反向查找原始注解，常见于根据注解类型做特定逻辑分支时
     * （比如分辨 {@link Asc} 和 {@link Desc} 都是 {@link com.lingyun.query.condition.OrderCondition}，
     * 但排序方向不同喵）。
     * </p>
     *
     * @return 标注了本元注解的那个原始注解类
     */
    Class<? extends Annotation> rawAnnotation();

    String name() default "";
}
