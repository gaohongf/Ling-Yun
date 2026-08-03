package com.lingyun.query.condition;

/**
 * 查询条件抽象基类.
 * <p>
 * 持有所属实体字段名 {@link #fieldName}，为所有具体查询条件提供公共属性。
 * 子类包括等值查询（{@link EqCondition}）、模糊查询（{@link LikeCondition}）、
 * 排序条件（{@link OrderCondition}）和范围查询（{@link ScopeCondition}），
 * 各自在此基础上扩展特定于自身类型的查询语义。
 * </p>
 *
 * @see QueryCondition
 */
public abstract class AbstractQueryCondition implements QueryCondition {
    private String fieldName;

    /**
     * 设置所属实体字段名.
     *
     * @param fieldName 字段名称
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 获取所属实体字段名.
     *
     * @return 字段名称
     */
    public String getFieldName() {
        return fieldName;
    }
}
