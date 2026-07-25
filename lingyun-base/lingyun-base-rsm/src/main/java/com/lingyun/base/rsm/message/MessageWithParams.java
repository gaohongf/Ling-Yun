package com.lingyun.base.rsm.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息键 + 格式化参数载体。
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageWithParams {
    /** 消息键，对应数据库中的 messageKey */
    private String message;
    /** 格式化参数，用于填充消息模板中的占位符（如 {0}、{1}） */
    private Object[] params;

    /**
     * 仅携带格式化参数创建实例。
     *
     * @param varargs 格式化参数
     * @return 新创建的 MessageWithParams 实例
     */
    public static MessageWithParams ofParams(Object... varargs) {
        MessageWithParams mwp = new MessageWithParams();
        mwp.params = varargs;
        return mwp;
    }

    /**
     * 仅携带消息键创建实例。
     *
     * @param msgId 消息键
     * @return 新创建的 MessageWithParams 实例
     */
    public static MessageWithParams ofMessage(String msgId) {
        MessageWithParams mwp = new MessageWithParams();
        mwp.message = msgId;
        return mwp;
    }

    /**
     * 设置消息键（流式 API，返回 this）。
     *
     * @param message 消息键
     * @return 当前实例，支持链式调用
     */
    public MessageWithParams msgId(String message) {
        this.message = message;
        return this;
    }

    /**
     * 设置格式化参数（流式 API，返回 this）。
     *
     * @param varargs 格式化参数
     * @return 当前实例，支持链式调用
     */
    public MessageWithParams params(Object... varargs) {
        this.params = varargs;
        return this;
    }
}
