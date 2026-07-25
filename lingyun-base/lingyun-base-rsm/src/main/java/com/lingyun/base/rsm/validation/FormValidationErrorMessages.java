package com.lingyun.base.rsm.validation;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 表单验证错误消息集合 — 字段名 → 错误消息。
 */
@Data
public class FormValidationErrorMessages {
    private Map<String, String> fields = new LinkedHashMap<>();

    /**
     * 添加字段错误（流式 API，返回 this 支持链式调用）。
     *
     * @param field   字段名
     * @param message 错误消息文本
     * @return 当前实例，支持链式添加多个错误
     */
    public FormValidationErrorMessages add(String field, String message) {
        fields.put(field, message);
        return this;
    }

    /**
     * 判断是否存在验证错误。
     *
     * @return 存在至少一个字段错误时返回 true
     */
    public boolean hasErrors() {
        return !fields.isEmpty();
    }
}
