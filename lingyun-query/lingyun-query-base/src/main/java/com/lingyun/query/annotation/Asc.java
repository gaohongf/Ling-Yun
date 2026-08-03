package com.lingyun.query.annotation;

import java.lang.annotation.*;

/**
 * 升序排序标记注解.
 * <p>
 * 用于查询 DSL 中声明字段的排序方向为正序（ASC），可标注在实体字段或查询方法参数上，
 * 框架在构建查询条件时自动识别并生成对应的 {@link OrderCondition}。
 * </p>
 *
 * @see Desc
 * @see Order
 * @see OrderCondition
 */
@Inherited
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Asc {
}
