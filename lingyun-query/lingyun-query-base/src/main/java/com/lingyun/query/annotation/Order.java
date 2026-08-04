package com.lingyun.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lingyun.query.condition.OrderCondition;

/**
 * 允许前端动态控制排序规则.
 * <p>
 * 标注在接收前端查询参数的字段或对象上，表示该字段的排序方向可由前端请求参数指定。
 * 配合 {@link Asc}、{@link Desc} 注解或 {@link OrderCondition} 使用，
 * 实现灵活的动态排序查询。
 * </p>
 *
 * @see Asc
 * @see Desc
 * @see OrderCondition
 */
@QueryAnnotation(conditionType = OrderCondition.class, rawAnnotation = Order.class)
@Inherited
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
}
