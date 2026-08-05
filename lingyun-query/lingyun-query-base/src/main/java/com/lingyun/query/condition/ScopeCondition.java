package com.lingyun.query.condition;

import java.util.Objects;

/**
 * 范围查询条件.
 * <p>
 * 对应 SQL 中的范围查询，等价于数学表达 {@code lowerLimit <[=] fieldName <[=] upperLimit}。
 * 支持四种比较语义：
 * <ul>
 *   <li>大于（{@code >}） — 仅设置下界且不包含等号</li>
 *   <li>大于等于（{@code >=}） — 仅设置下界且包含等号</li>
 *   <li>小于（{@code <}） — 仅设置上界且不包含等号</li>
 *   <li>小于等于（{@code <=}） — 仅设置上界且包含等号</li>
 * </ul>
 * 同时设置上界和下界时，表示 BETWEEN 区间查询。
 * </p>
 *
 * @param <T> 范围值的类型（通常为数值或日期类型）
 * @see QueryCondition
 */
public class ScopeCondition<T> extends AbstractQueryCondition {
    private T upperLimit;
    private boolean equalsUpperLimit;
    private T lowerLimit;
    private boolean equalsLowerLimit;

    /**
     * 判断是否存在下界且为开区间（仅大于，不含等于）.
     *
     * @return 如果下界不为 {@code null} 且不包含等号则返回 {@code true}
     */
    public boolean isGt(){
        return !equalsLowerLimit && this.lowerLimit != null ;
    }

    /**
     * 判断是否存在上界且为开区间（仅小于，不含等于）.
     *
     * @return 如果上界不为 {@code null} 且不包含等号则返回 {@code true}
     */
    public boolean isLt(){
        return !equalsUpperLimit && this.upperLimit != null ;
    }

    /**
     * 判断是否存在下界且为闭区间（大于等于）.
     *
     * @return 如果下界不为 {@code null} 且包含等号则返回 {@code true}
     */
    public boolean isGe(){
        return equalsLowerLimit && this.lowerLimit != null;
    }

    /**
     * 判断是否存在上界且为闭区间（小于等于）.
     *
     * @return 如果上界不为 {@code null} 且包含等号则返回 {@code true}
     */
    public boolean isLe(){
        return equalsUpperLimit && this.upperLimit != null;
    }

    /**
     * 设置下界为开区间（仅大于）.
     *
     * @param lowerLimit 下界值
     */
    public void setGt(T lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    /**
     * 设置上界为开区间（仅小于）.
     *
     * @param upperLimit 上界值
     */
    public void setLt(T upperLimit) {
        this.upperLimit = upperLimit;
    }

    /**
     * 设置上界为闭区间（小于等于）.
     *
     * @param upperLimit 上界值
     */
    public void setLe(T upperLimit) {
        this.upperLimit = upperLimit;
        this.equalsUpperLimit = true;
    }

    /**
     * 设置下界为闭区间（大于等于）.
     *
     * @param lowerLimit 下界值
     */
    public void setGe(T lowerLimit) {
        this.lowerLimit = lowerLimit;
        this.equalsLowerLimit = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScopeCondition<?> that = (ScopeCondition<?>) o;
        return Objects.equals(upperLimit, that.upperLimit) && Objects.equals(lowerLimit, that.lowerLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upperLimit, lowerLimit);
    }

    @Override
    public String toString() {
        String field = getFieldName();
        if (lowerLimit != null && upperLimit != null) {
            return lowerLimit + (isGe() ? " <= " : " < ")
                    + field + (isLe() ? " <= " : " < ") + upperLimit;
        }
        if (lowerLimit != null) {
            return field + (isGe() ? " >= " : " > ") + lowerLimit;
        }
        if (upperLimit != null) {
            return field + (isLe() ? " <= " : " < ") + upperLimit;
        }
        return field + " (no bounds)";
    }
}
