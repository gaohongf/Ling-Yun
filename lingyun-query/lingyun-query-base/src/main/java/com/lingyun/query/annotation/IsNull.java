package com.lingyun.query.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;

import com.lingyun.query.condition.IsNullCondition;

/**
 * SQL {@code IS NULL} 查询标记注解.
 * <p>
 * 用于查询 DSL 中声明字段采用"判空"匹配（即 SQL 的 {@code WHERE field IS NULL}），
 * 可标注在实体字段或查询方法参数上。
 * 标注此注解的字段无需传值（由 {@link OmitValueClause @OmitValueClause} 声明），
 * 框架在解析时会自动生成对应的 {@link IsNullCondition}——字段为空即命中，最适合用来揪出那些"应该有但还没填"的记录喵~
 * </p>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * public class UserQueryCriteria {
 *     @IsNull
 *     private String deletedAt;  // 对应 WHERE deleted_at IS NULL（软删除未删除的记录）
 * }
 * }</pre>
 * <p>
 * 与 {@link IsNotNull} 互为镜像查询——一个查"为空"，一个查"不为空"，
 * 两条猫猫尾巴各扫一边喵~
 * </p>
 *
 * @see IsNullCondition
 * @see IsNotNull
 * @see QueryAnnotation
 */
@QueryAnnotation(conditionType = IsNullCondition.class, rawAnnotation = IsNull.class)
@OmitValueClause
@Inherited
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNull {
    @AliasFor(annotation = QueryAnnotation.class, attribute = "name")
    String value() default "";

}
