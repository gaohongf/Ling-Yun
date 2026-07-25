package com.lingyun.base.rsm.validation;

import jakarta.validation.ConstraintViolation;

/**
 * 简单的 ConstraintViolation Context 包装，供 DatabaseMessageInterpolator 使用。
 */
public class SimpleMessageInterpolatorContext implements jakarta.validation.MessageInterpolator.Context {
    private final ConstraintViolation<?> violation;

    /**
     * 构造 Context，从 ConstraintViolation 中提取约束信息。
     *
     * @param violation 约束违例实例，提供了约束描述符和非法值
     */
    public SimpleMessageInterpolatorContext(ConstraintViolation<?> violation) {
        this.violation = violation;
    }

    /**
     * 获取约束描述符，包含约束注解的元数据。
     *
     * @return 约束描述符，如 @NotNull 的 Descriptor
     */
    @Override
    public jakarta.validation.metadata.ConstraintDescriptor<?> getConstraintDescriptor() {
        return violation.getConstraintDescriptor();
    }

    /**
     * 获取被验证的（非法）值。
     *
     * @return 触发约束违例的值
     */
    @Override
    public Object getValidatedValue() {
        return violation.getInvalidValue();
    }

    /**
     * 不支持的类型转换操作。
     *
     * @param type 目标类型
     * @param <T>  类型参数
     * @return 永远不会正常返回
     * @throws UnsupportedOperationException 始终抛出，此实现不支持 unwrap
     */
    @Override
    public <T> T unwrap(Class<T> type) {
        throw new UnsupportedOperationException();
    }
}
