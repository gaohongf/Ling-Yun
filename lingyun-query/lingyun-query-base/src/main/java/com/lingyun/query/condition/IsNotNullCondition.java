package com.lingyun.query.condition;

/**
 * SQL {@code IS NOT NULL} 查询条件.
 * <p>
 * 对应 SQL 中的 {@code WHERE fieldName IS NOT NULL} 子句。
 * 继承自 {@link AbstractQueryCondition}，仅持有字段名——无值语义，
 * 由 {@link QueryConditionUtils} 在遇到 {@link com.lingyun.query.annotation.IsNotNull @IsNotNull}
 * 注解时直接实例化喵。
 * </p>
 *
 * <h3>与注解的映射</h3>
 * <p>
 * 该条件对应 {@link com.lingyun.query.annotation.IsNotNull @IsNotNull} 注解，
 * 由 {@code @QueryAnnotation(conditionType = IsNotNullCondition.class)} 声明映射关系。
 * 由于 {@code IS NOT NULL} 不需要参数值，注解上额外标注了
 * {@link com.lingyun.query.annotation.OmitValueClause @OmitValueClause}，
 * 解析器不会尝试从请求体中读取该字段的值喵。
 * </p>
 *
 * <h3>与 {@link IsNullCondition} 的区别</h3>
 * <p>
 * {@link IsNullCondition} 查的是"字段为空"的记录，
 * 本类查的是"字段不为空"的记录——两条猫猫触须各探一边喵~
 * </p>
 *
 * @see AbstractQueryCondition
 * @see QueryCondition
 * @see IsNullCondition
 * @see com.lingyun.query.annotation.IsNotNull
 */
public class IsNotNullCondition extends AbstractQueryCondition {

    /**
     * 构造一个 IS NOT NULL 条件，并绑定字段名.
     * <p>
     * 由 {@link QueryConditionUtils#getCondition} 在注解调度时调用，
     * 不需要值参数——因为 {@code IS NOT NULL} 本身就是完整语义喵。
     * </p>
     *
     * @param fieldName 实体字段名
     */
    public IsNotNullCondition(String fieldName) {
        setFieldName(fieldName);
    }

    /**
     * 生成人类可读的子句表示，形如 {@code fieldName IS NOT NULL}.
     * <p>
     * 注意：此输出仅用于调试和日志喵。
     * </p>
     *
     * @return 形如 {@code fieldName IS NOT NULL} 的字符串
     */
    @Override
    public String toString() {
        return getFieldName() + " IS NOt NULL";
    }
}
