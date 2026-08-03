package com.lingyun.query.condition;

/**
 * 模糊匹配模式枚举.
 * <p>
 * 定义 SQL LIKE 查询中通配符添加的三种模式，用于配合 {@link LikeCondition} 使用：
 * <ul>
 *   <li>{@link #LIKE_LEFT} — 左模糊匹配，在值左侧追加 {@code %}（{@code %value}），匹配以指定值结尾的结果</li>
 *   <li>{@link #LIKE_RIGHT} — 右模糊匹配，在值右侧追加 {@code %}（{@code value%}），匹配以指定值开头的结果</li>
 *   <li>{@link #LIKE} — 全模糊匹配，在值两侧均追加 {@code %}（{@code %value%}），匹配包含指定值的结果</li>
 * </ul>
 * </p>
 *
 * @see LikeCondition
 */
public enum LikeOption {
    /** 左模糊：值左侧添加通配符（{@code %value}），匹配以指定值结尾 */
    LIKE_LEFT,
    /** 右模糊：值右侧添加通配符（{@code value%}），匹配以指定值开头 */
    LIKE_RIGHT,
    /** 全模糊：值两侧均添加通配符（{@code %value%}），匹配包含指定值 */
    LIKE
}
