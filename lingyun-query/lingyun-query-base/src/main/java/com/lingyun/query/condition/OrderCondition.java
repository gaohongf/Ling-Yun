package com.lingyun.query.condition;

/**
 * 排序条件.
 * <p>
 * 封装单个字段的排序规则，包含排序字段名（继承自 {@link AbstractQueryCondition#getFieldName()}）
 * 和排序方向（{@link OrderOption}）。对应 SQL 中的 {@code ORDER BY fieldName ASC/DESC}。
 * </p>
 *
 * @see OrderOption
 * @see QueryCondition
 */
public class OrderCondition extends AbstractQueryCondition {
    private OrderOption option;

    /**
     * 设置排序方向.
     *
     * @param option 排序方向，ASC 或 DESC
     */
    public void setOption(OrderOption option) {
        this.option = option;
    }

    /**
     * 获取排序方向.
     *
     * @return 当前排序方向
     */
    public OrderOption getOption() {
        return option;
    }

    @Override
    public String toString() {
        return "ORDER BY " + getFieldName() + " " + option;
    }
}
