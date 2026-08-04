package com.lingyun.query.annotation;

import java.lang.annotation.*;

import com.lingyun.query.condition.ScopeCondition;

/**
 * 范围查询标记注解.
 * <p>
 * 用于查询 DSL 中声明字段为范围查询，支持大于（&gt;）、小于（&lt;）、大于等于（&gt;=）、
 * 小于等于（&lt;=）及其组合（BETWEEN），可标注在实体字段或查询方法参数上，
 * 框架在构建查询条件时自动生成对应的 {@link ScopeCondition}。
 * </p>
 *
 * @see ScopeCondition
 */
@QueryAnnotation(conditionType = ScopeCondition.class, rawAnnotation = Scope.class)
@Inherited
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
}
