package com.lingyun.query.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;

import com.lingyun.query.condition.IsNotNullCondition;

/**
 * SQL {@code IS NOT NULL} 查询标记注解.
 * <p>
 * 用于查询 DSL 中声明字段采用"非空"匹配（即 SQL 的 {@code WHERE field IS NOT NULL}），
 * 可标注在实体字段或查询方法参数上。
 * 标注此注解的字段无需传值（由 {@link OmitValueClause @OmitValueClause} 声明），
 * 框架在解析时会自动生成对应的 {@link IsNotNullCondition}——字段非空即命中，猫猫觉得很清爽喵~
 * </p>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * public class UserQueryCriteria {
 *     @IsNotNull
 *     private String email;  // 对应 WHERE email IS NOT NULL
 * }
 * }</pre>
 * <p>
 * 与 {@link IsNull} 互为镜像查询——一个查"不为空"，一个查"为空"，
 * 双猫合璧覆盖 SQL 中最常用的空值判断场景喵~
 * </p>
 *
 * @see IsNotNullCondition
 * @see IsNull
 * @see QueryAnnotation
 */
@QueryAnnotation(conditionType = IsNotNullCondition.class, rawAnnotation = IsNotNull.class)
@OmitValueClause
@Inherited
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNotNull {
    @AliasFor(annotation = QueryAnnotation.class, attribute = "name")
    String value() default "";

}
