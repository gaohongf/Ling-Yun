package com.lingyun.query.condition;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 小于等于（&lt;=）查询——{@link ScopeCondition} 的便捷子类。
 * <p>
 * JSON 反序列化时自动映射为闭区间上界查询喵。
 *
 * <pre>{@code
 * // JSON: {"maxAge": 60}
 * // 等价于: fieldName <= 60
 * Le<Integer> le = new Le<>(60);
 * }</pre>
 *
 * @param <T> 值类型
 * @see ScopeCondition
 * @see Lt
 */
public class Le<T> extends ScopeCondition<T> {

    /**
     * 构造小于等于条件。
     *
     * @param value 上界值（含等号）
     */
    @JsonCreator
    public Le(T value) {
        setLe(value);
    }
}
