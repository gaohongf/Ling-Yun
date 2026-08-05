package com.lingyun.query.condition;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lingyun.query.criteria.QueryRequestNormalizationFailedException;

/**
 * BETWEEN 区间查询——{@link ScopeCondition} 的便捷子类，自动闭区间双向。
 * <p>
 * 传入一个恰好包含两个元素的数组 {@code [1, 10]}，自动解析为
 * {@code fieldName >= 最小值 AND fieldName <= 最大值}，无需手动选择 setGe/setLe。
 * 构造函数会自动比较两个值的大小，确保较小的值映射为下界、较大的值映射为上界喵。
 *
 * <h3>示例</h3>
 * <pre>{@code
 * // JSON 请求体: {"price": [100, 500]}
 * // 等价于: price >= 100 AND price <= 500
 * Between<Integer> between = new Between<>(List.of(100, 500));
 * }</pre>
 *
 * @param <T> 区间值的类型（必须实现 {@link Comparable}）
 * @see ScopeCondition
 */
public class Between<T> extends ScopeCondition<T> {

    /**
     * 从两元素列表构造 BETWEEN 条件。
     * <p>
     * 自动比较两值大小，较小的映射为闭区间下界（{@code >=}），
     * 较大的映射为闭区间上界（{@code <=}），实现双向闭区间查询。
     *
     * @param value 恰好两个非空元素的可比较值列表，如 {@code [100, 500]}
     * @throws QueryRequestNormalizationFailedException 如果列表为空/长度不为2/元素非可比较类型
     */
    @JsonCreator
    public Between(List<T> value) {
        T t1;
        T t2;
        if (value == null || value.size() != 2 || (t1 = value.get(0)) == null || (t2 = value.get(1)) == null) {
            throw new QueryRequestNormalizationFailedException("between 需要包含两个非空的值");
        }
        if (t1 instanceof Comparable t1Comparable && t2 instanceof Comparable) {
            if (t1Comparable.compareTo(t2) == 1) {
                setGe(t2);
                setLe(t1);
            } else {
                setGe(t1);
                setLe(t2);
            }
        } else {
            throw new QueryRequestNormalizationFailedException("between 需要包含两个可比较的值");
        }
    }
}
