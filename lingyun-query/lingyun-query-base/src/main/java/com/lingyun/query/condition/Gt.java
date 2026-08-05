package com.lingyun.query.condition;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 大于（&gt;）查询——{@link ScopeCondition} 的便捷子类。
 * <p>
 * JSON 反序列化时自动映射为开区间下界查询喵。
 *
 * <pre>{@code
 * // JSON: {"minPrice": 100}
 * // 等价于: fieldName > 100
 * Gt<Integer> gt = new Gt<>(100);
 * }</pre>
 *
 * @param <T> 值类型
 * @see ScopeCondition
 * @see Ge
 */
public class Gt<T> extends ScopeCondition<T> {

    /**
     * 构造大于条件。
     *
     * @param value 下界值（不含等号）
     */
    @JsonCreator
    public Gt(T value) {
        setGt(value);
    }
}
