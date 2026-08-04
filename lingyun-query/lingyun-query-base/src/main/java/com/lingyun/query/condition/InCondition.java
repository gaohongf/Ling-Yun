package com.lingyun.query.condition;

import java.util.Collection;
import java.util.StringJoiner;

/**
 * SQL {@code IN} 查询条件.
 * <p>
 * 对应 SQL 中的 {@code WHERE fieldName IN (v1, v2, ...)} 子句。
 * 继承自 {@link AbstractQueryCondition}，在其基础上封装一个元素集合 {@link #getElements()}，
 * 表示 IN 列表中的所有候选值。字段名由基类的 {@link AbstractQueryCondition#getFieldName()} 提供。
 * </p>
 *
 * <h3>数据来源</h3>
 * <p>
 * 元素的 Java 类型由泛型 {@code T} 控制，原始数据可以是数组（如 {@code int[]}、{@code String[]}）
 * 或 Collection 子类（如 {@code List}、{@code Set}）。框架在 {@link QueryConditionUtils#getCondition}
 * 中统一将数组转换为 Collection 后再注入。
 * </p>
 *
 * <h3>与注解的映射</h3>
 * <p>
 * 该条件对应 {@link com.lingyun.query.annotation.In @In} 注解，
 * 由 {@link com.lingyun.query.annotation.QueryAnnotation @QueryAnnotation(conditionType = InCondition.class)}
 * 声明映射关系喵。
 * </p>
 *
 * @param <T> IN 列表中元素的 Java 类型
 * @see AbstractQueryCondition
 * @see QueryCondition
 * @see com.lingyun.query.annotation.In
 */
public class InCondition<T> extends AbstractQueryCondition {
    private Collection<T> elements;

    /**
     * 设置 IN 列表的元素集合.
     * <p>
     * 通常由 {@link QueryConditionUtils} 在构建条件时调用，
     * 传入从请求 JSON 中解析出来的数组或集合数据。
     * </p>
     *
     * @param elements IN 列表中的候选值集合
     */
    public void setElements(Collection<T> elements) {
        this.elements = elements;
    }

    /**
     * 获取 IN 列表的元素集合.
     *
     * @return IN 列表中的候选值集合，可能为 {@code null}
     */
    public Collection<T> getElements() {
        return elements;
    }

    /**
     * 生成人类可读的子句表示，形如 {@code name IN (v1, v2, v3)}.
     * <p>
     * 注意：此输出仅用于调试和日志喵。
     * </p>
     *
     * @return 形如 {@code name IN (元素1, 元素2, ...)} 的字符串
     */
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        elements.forEach(e -> joiner.add(e.toString()));
        return getFieldName() + " IN (" + joiner + ")";
    }
}
