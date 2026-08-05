package com.lingyun.query.condition;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 大于等于（&gt;=）查询——{@link ScopeCondition} 的便捷子类。
 * <p>
 * JSON 反序列化时自动映射为闭区间下界查询喵。
 *
 * <pre>{@code
 * // JSON: {"minAge": 18}
 * // 等价于: fieldName >= 18
 * Ge<Integer> ge = new Ge<>(18);
 * }</pre>
 *
 * @param <T> 值类型
 * @see ScopeCondition
 * @see Between
 */
public class Ge<T> extends ScopeCondition<T> {

    /**
     * 构造大于等于条件。
     *
     * @param value 下界值（含等号）
     */
    @JsonCreator
    public Ge(T value) {
        setGe(value);
    }
}
