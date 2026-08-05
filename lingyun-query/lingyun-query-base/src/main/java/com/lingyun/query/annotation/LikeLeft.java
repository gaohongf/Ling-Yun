package com.lingyun.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.lingyun.query.condition.LikeCondition;

/**
 * 左模糊匹配标记注解.
 * <p>
 * 用于查询 DSL 中声明字段采用左模糊匹配（{@code %value}），即仅匹配以指定值结尾的结果，
 * 可标注在实体字段或查询方法参数上，框架在构建查询条件时自动生成对应的 {@link LikeCondition}
 * 并将匹配模式设为 {@link LikeOption#LIKE_LEFT}。
 * </p>
 *
 * @see Like
 * @see LikeRight
 * @see LikeCondition
 * @see LikeOption
 */
@QueryAnnotation(conditionType = LikeCondition.class, rawAnnotation = LikeLeft.class)
@Inherited
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LikeLeft {
    
    @AliasFor(annotation = QueryAnnotation.class, attribute = "name")
    String value() default "";

}
