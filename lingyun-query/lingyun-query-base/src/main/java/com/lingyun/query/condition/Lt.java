package com.lingyun.query.condition;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 小于（&lt;）查询——{@link ScopeCondition} 的便捷子类。
 * <p>
 * JSON 反序列化时自动映射为开区间上界查询喵。
 *
 * <pre>{@code
 * // JSON: {"maxPrice": 999}
 * // 等价于: fieldName < 999
 * Lt<Integer> lt = new Lt<>(999);
 * }</pre>
 *
 * @param <T> 值类型
 * @see ScopeCondition
 * @see Le
 */
public class Lt<T> extends ScopeCondition<T> {

    /**
     * 构造小于条件。
     *
     * @param value 上界值（不含等号）
     */
    @JsonCreator
    public Lt(T value) {
        setLt(value);
    }
}
