package com.lingyun.query.condition;

/**
 * 查询逻辑关系——控制 {@link ConditionNode} 中子条件的拼接方式。
 *
 * @see ConditionNode
 */
public enum QueryLogic {

    /** 或（||）——满足任一条件即匹配 */
    OR,

    /** 且（&&）——所有条件必须同时满足 */
    AND
}
