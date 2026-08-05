package com.lingyun.query.condition;

/**
 * SQL {@code IS NULL} 查询条件.
 * <p>
 * 对应 SQL 中的 {@code WHERE fieldName IS NULL} 子句。
 * 继承自 {@link AbstractQueryCondition}，仅持有字段名——无值语义，
 * 由 {@link QueryConditionUtils} 在遇到 {@link com.lingyun.query.annotation.IsNull @IsNull}
 * 注解时直接实例化喵。
 * </p>
 *
 * <h3>与注解的映射</h3>
 * <p>
 * 该条件对应 {@link com.lingyun.query.annotation.IsNull @IsNull} 注解，
 * 由 {@code @QueryAnnotation(conditionType = IsNullCondition.class)} 声明映射关系。
 * 由于 {@code IS NULL} 不需要参数值，注解上额外标注了
 * {@link com.lingyun.query.annotation.OmitValueClause @OmitValueClause}，
 * 解析器不会尝试从请求体中读取该字段的值喵。
 * </p>
 *
 * <h3>典型用途</h3>
 * <p>
 * 常用于查询"尚未设置某字段"的记录（如软删除标记、审批时间、完成时间等），
 * 猫猫用它来嗅探那些空荡荡的字段喵~
 * </p>
 *
 * @see AbstractQueryCondition
 * @see QueryCondition
 * @see IsNotNullCondition
 * @see com.lingyun.query.annotation.IsNull
 */
public class IsNullCondition extends AbstractQueryCondition {

    /**
     * 构造一个 IS NULL 条件，并绑定字段名.
     * <p>
     * 由 {@link QueryConditionUtils#getCondition} 在注解调度时调用，
     * 不需要值参数——因为 {@code IS NULL} 本身就是完整语义喵。
     * </p>
     *
     * @param fieldName 实体字段名
     */
    public IsNullCondition(String fieldName) {
        setFieldName(fieldName);
    }

    /**
     * 生成人类可读的子句表示，形如 {@code fieldName IS NULL}.
     * <p>
     * 注意：此输出仅用于调试和日志喵。
     * </p>
     *
     * @return 形如 {@code fieldName IS NULL} 的字符串
     */
    @Override
    public String toString() {
        return getFieldName() + " IS NULL";
    }
}
