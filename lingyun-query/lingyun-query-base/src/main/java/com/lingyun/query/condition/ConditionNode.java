package com.lingyun.query.condition;

import java.util.Collection;

/**
 * 条件节点（魔力导线）——把多个 {@link QueryCondition} 阵眼按 {@link QueryLogic} 编织在一起。
 * <p>
 * 单个 {@link QueryCondition}（如 {@code EqCondition}、{@code LikeCondition}）只能表示一个
 * 原子条件，像猫猫只会喵喵叫一样简单喵。当需要 {@code (a = 1 AND b LIKE '%喵%') OR c > 100}
 * 这种复杂魔法阵时，就需要 {@code ConditionNode} 来嵌套串联。
 * <p>
 * 默认逻辑为 {@link QueryLogic#AND}，即阵眼之间是"并且"关系。
 *
 * <h3>示例</h3>
 * <pre>{@code
 * // 简单阵眼：name = '喵' OR name = '咪'
 * ConditionNode node = new ConditionNode();
 * node.setLogic(QueryLogic.OR);
 * node.setConditions(List.of(
 *     new EqCondition().setValue("喵"),
 *     new EqCondition().setValue("咪")
 * ));
 *
 * // 嵌套魔法阵：(age > 18 AND age < 60) AND (name LIKE '%喵%')
 * ConditionNode ageRange = new ConditionNode();
 * ageRange.setLogic(QueryLogic.AND);
 * ageRange.setConditions(List.of(gt18, lt60));
 *
 * ConditionNode root = new ConditionNode();
 * root.setLogic(QueryLogic.AND);
 * root.setConditions(List.of(ageRange, likeName));
 * }</pre>
 *
 * @see QueryLogic
 * @see QueryCondition
 */
public class ConditionNode implements QueryCondition {

    /** 当前节点下的子条件集合（可以是原子条件或嵌套的 ConditionNode） */
    private Collection<QueryCondition> conditions;

    /** 子条件之间的逻辑关系：AND（默认）或 OR */
    private QueryLogic logic = QueryLogic.AND;

    /**
     * 设置子条件集合。
     *
     * @param conditions 子条件集合，元素可以是原子条件或嵌套的 {@code ConditionNode}
     */
    public void setConditions(Collection<QueryCondition> conditions) {
        this.conditions = conditions;
    }

    /**
     * 设置子条件之间的逻辑关系。
     *
     * @param logic AND 或 OR，默认 AND
     */
    public void setLogic(QueryLogic logic) {
        this.logic = logic;
    }

    /**
     * 获取子条件集合。
     *
     * @return 子条件集合，可能为 {@code null}
     */
    public Collection<QueryCondition> getConditions() {
        return conditions;
    }

    /**
     * 获取逻辑关系。
     *
     * @return AND 或 OR，默认 AND
     */
    public QueryLogic getLogic() {
        return logic;
    }
}
