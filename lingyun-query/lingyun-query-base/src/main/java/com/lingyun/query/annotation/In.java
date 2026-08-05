package com.lingyun.query.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;

import com.lingyun.query.condition.InCondition;

/**
 * SQL {@code IN (...)} 查询标记注解.
 * <p>
 * 用于查询 DSL 中声明字段采用集合包含匹配（即 SQL 的 {@code WHERE field IN (v1, v2, ...)}），
 * 可标注在实体字段或查询方法参数上。
 * 被标注的字段类型应为数组（如 {@code int[]}）或 {@link java.util.Collection} 子类，
 * 框架在解析时会自动将其转换为对应的 {@link InCondition}。
 * </p>
 *
 * <h3>使用示例</h3>
 * 
 * <pre>{@code
 * public class UserQueryCriteria {
 *     @In
 *     private Long[] ids; // 对应 WHERE id IN (1, 2, 3)
 *
 *     @In
 *     private List<String> names; // 对应 WHERE name IN ('zhang', 'li')
 * }
 * }</pre>
 * <p>
 * 注意喵：和 {@link Like}、{@link Scope} 一样，这是用注解帮 Service 远离 HTTP 层耦合的猫猫拳法喵~
 * </p>
 *
 * @see InCondition
 * @see QueryAnnotation
 */
@QueryAnnotation(conditionType = InCondition.class, rawAnnotation = In.class)
@Inherited
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface In {
    @AliasFor(annotation = QueryAnnotation.class, attribute = "name")
    String value() default "";
}
