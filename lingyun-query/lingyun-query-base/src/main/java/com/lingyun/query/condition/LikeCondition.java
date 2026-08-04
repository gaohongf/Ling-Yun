package com.lingyun.query.condition;

/**
 * 模糊查询条件.
 * <p>
 * 对应 SQL 中的 {@code LIKE} 操作，支持三种模糊匹配模式，
 * 由 {@link LikeOption} 枚举控制：
 * <ul>
 *   <li>{@link LikeOption#LIKE LIKE} — 全模糊匹配（{@code %value%}）</li>
 *   <li>{@link LikeOption#LIKE_LEFT LIKE_LEFT} — 左模糊匹配（{@code %value}）</li>
 *   <li>{@link LikeOption#LIKE_RIGHT LIKE_RIGHT} — 右模糊匹配（{@code value%}）</li>
 * </ul>
 * 默认匹配模式为 {@link LikeOption#LIKE}。
 * </p>
 *
 * @param <T> 字段值类型
 * @see LikeOption
 * @see QueryCondition
 */
public class LikeCondition<T> extends AbstractQueryCondition {
    private T value;
    private LikeOption option = LikeOption.LIKE;

    /**
     * 设置模糊匹配模式.
     *
     * @param option 匹配模式，可为 LIKE、LIKE_LEFT 或 LIKE_RIGHT
     */
    public void setOption(LikeOption option) {
        this.option = option;
    }

    /**
     * 获取模糊匹配模式.
     *
     * @return 当前匹配模式，默认为 LIKE
     */
    public LikeOption getOption() {
        return option;
    }

    /**
     * 获取模糊查询值.
     *
     * @return 模糊查询的值（不含通配符，由框架根据 {@link #getOption()} 自动拼接）
     */
    public T getValue() {
        return value;
    }

    /**
     * 设置模糊查询值.
     *
     * @param value 模糊查询的值（不含通配符）
     */
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String pattern = switch (option) {
            case LIKE_LEFT  -> "%" + value;
            case LIKE_RIGHT -> value + "%";
            default         -> "%" + value + "%";
        };
        return getFieldName() + " LIKE '" + pattern + "'";
    }
}
