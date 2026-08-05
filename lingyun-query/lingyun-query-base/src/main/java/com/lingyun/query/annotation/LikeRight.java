package com.lingyun.query.annotation;

import java.lang.annotation.*;

import org.springframework.core.annotation.AliasFor;

import com.lingyun.query.condition.LikeCondition;
import org.springframework.core.annotation.AliasFor;

import com.lingyun.query.condition.LikeOption;

/**
 * 右模糊匹配标记注解.
 * <p>
 * 用于查询 DSL 中声明字段采用右模糊匹配（{@code value%}），即仅匹配以指定值开头的结果，
 * 可标注在实体字段或查询方法参数上，框架在构建查询条件时自动生成对应的 {@link LikeCondition}
 * 并将匹配模式设为 {@link LikeOption#LIKE_RIGHT}。
 * </p>
 *
 * @see Like
 * @see LikeLeft
 * @see LikeCondition
 * @see LikeOption
 */
@QueryAnnotation(conditionType = LikeCondition.class, rawAnnotation = LikeRight.class)
@Inherited
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface LikeRight {
    @AliasFor(annotation = QueryAnnotation.class, attribute = "name")
    String value() default "";

}
