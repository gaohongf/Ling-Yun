package com.lingyun.query.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;

import com.lingyun.query.condition.NotInCondition;

/**
 * SQL {@code NOT IN (...)} 查询标记注解.
 * <p>
 * 用于查询 DSL 中声明字段采用集合排除匹配（即 SQL 的 {@code WHERE field NOT IN (v1, v2, ...)}），
 * 可标注在实体字段或查询方法参数上。
 * 被标注的字段类型应为数组（如 {@code Long[]}）或 {@link java.util.Collection} 子类，
 * 框架在解析时会自动将其转换为对应的 {@link NotInCondition}——从结果集中排除集合内的所有值喵。
 * </p>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * public class UserQueryCriteria {
 *     @NotIn
 *     private Long[] excludeIds;       // 对应 WHERE id NOT IN (1, 2, 3)
 *
 *     @NotIn
 *     private List<String> blacklist;  // 对应 WHERE name NOT IN ('spam', 'bot')
 * }
 * }</pre>
 * <p>
 * 与 {@link com.lingyun.query.annotation.In @In} 互为镜像——{@code IN} 是白名单，{@code NOT IN} 是黑名单，
 * 猫猫帮你把不需要的全踢掉喵~
 * </p>
 *
 * @see NotInCondition
 * @see com.lingyun.query.annotation.In
 * @see QueryAnnotation
 */
@QueryAnnotation(conditionType = NotInCondition.class, rawAnnotation = NotIn.class)
@Inherited
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotIn {
    @AliasFor(annotation = QueryAnnotation.class, attribute = "name")
    String value() default "";

}
