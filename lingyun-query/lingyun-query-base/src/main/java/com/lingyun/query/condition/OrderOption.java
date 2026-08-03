package com.lingyun.query.condition;

/**
 * 排序方向枚举.
 * <p>
 * 定义 SQL ORDER BY 子句中的排序方向，用于配合 {@link OrderCondition} 使用：
 * <ul>
 *   <li>{@link #ASC} — 升序排列，对应 SQL 中的 {@code ASC}</li>
 *   <li>{@link #DESC} — 降序排列，对应 SQL 中的 {@code DESC}</li>
 * </ul>
 * </p>
 *
 * @see OrderCondition
 */
public enum OrderOption {
    /** 升序 */
    ASC,
    /** 降序 */
    DESC
}
