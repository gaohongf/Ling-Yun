package com.lingyun.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 全模糊匹配标记注解.
 * <p>
 * 用于查询 DSL 中声明字段采用全模糊匹配（{@code %value%}），可标注在实体字段或查询方法参数上，
 * 框架在构建查询条件时自动生成对应的 {@link LikeCondition} 并将匹配模式设为 {@link LikeOption#LIKE}。
 * </p>
 *
 * @see LikeLeft
 * @see LikeRight
 * @see LikeCondition
 * @see LikeOption
 */
@Inherited
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Like {
}
