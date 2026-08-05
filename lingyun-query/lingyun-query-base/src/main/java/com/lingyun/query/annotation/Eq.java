package com.lingyun.query.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;

import com.lingyun.query.condition.EqCondition;

/**
 * 等值查询标记注解.
 * <p>
 * 用于查询 DSL 中声明字段采用精确匹配（即 SQL 的 {@code WHERE field = value}），
 * 可标注在实体字段或查询方法参数上，框架在构建查询条件时自动生成对应的 {@link EqCondition}。
 * </p>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * public class UserQueryCriteria {
 *     @Eq                            // 默认使用 Java 字段名 → WHERE status = value
 *     private Integer status;
 *
 *     @Eq("user_name")               // 别名映射 → WHERE user_name = value
 *     private String name;
 * }
 * }</pre>
 * <p>
 * {@link #value()} 通过 {@link AliasFor @AliasFor} 映射到
 * {@link QueryAnnotation#name()}，用于前端传入名和查询字段名不同时指定别名喵~
 * </p>
 *
 * @see EqCondition
 * @see QueryAnnotation
 */
@QueryAnnotation(conditionType = EqCondition.class, rawAnnotation = Eq.class)
@Inherited
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Eq {

    /**
     * 查询字段别名——当前端传入名与数据库字段名不一致时指定。
     * <p>
     * 默认空字符串表示使用 Java 字段名。
     *
     * @return 别名（数据库列名），默认 ""
     */
    @AliasFor(annotation = QueryAnnotation.class, attribute = "name")
    String value() default "";
}
