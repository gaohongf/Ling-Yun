package com.lingyun.query.condition;

/**
 * 等值查询条件.
 * <p>
 * 对应 SQL 中的 {@code =} 判断，用于精确匹配单一字段值。
 * 泛型 {@code T} 表示字段值的 Java 类型（通常为 String、Integer 等）。
 * 当 {@link #getValue()} 返回 {@code null} 时，该条件通常会被忽略。
 * </p>
 *
 * @param <T> 字段值类型
 * @see QueryCondition
 */
public class EqCondition<T> extends AbstractQueryCondition {
    private T value;

    /**
     * 获取查询值.
     *
     * @return 等值查询的值
     */
    public T getValue() {
        return value;
    }

    /**
     * 设置查询值.
     *
     * @param value 等值查询的值
     */
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getFieldName() + " = " + value;
    }
}
