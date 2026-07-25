package com.lingyun.base.rsm.exception;

import lombok.Getter;

/**
 * 业务请求异常 — 携带消息键和参数，由上层响应框架解析为统一响应体。
 * <p>
 * 使用 {@code R.error("msg_key", arg1, arg2)} 抛出，
 * 而非 {@code throw new RequestException(...)} 直接使用。
 */
@Getter
public class RequestException extends RuntimeException {

    private final String msgId;
    private final Object[] varargs;

    /**
     * 构造业务请求异常。
     * <p>
     * {@code msgId} 是消息键（对应数据库中的 message_key），由上层响应框架查询模板后填充占位符。
     * 与标准异常不同，此处的 {@code msgId} 不是最终展示给用户的文本，而是消息的标识键。
     *
     * @param msgId   消息键（对应数据库中的 message_key）
     * @param varargs 消息模板中的占位符参数，按顺序替换 {0}, {1}, ...
     */
    public RequestException(String msgId, Object... varargs) {
        super(msgId);
        this.msgId = msgId;
        this.varargs = varargs;
    }

    /**
     * 构造携带原始异常（cause）的业务请求异常。
     * <p>
     * {@code throwable} 作为 cause 保留完整堆栈，便于排查问题。
     * {@code msgId} 仍是消息键，不是最终展示文本。
     *
     * @param throwable 原始异常（作为 cause 保留完整堆栈）
     * @param msgId     消息键（对应数据库中的 message_key）
     * @param varargs   消息模板中的占位符参数，按顺序替换 {0}, {1}, ...
     */
    public RequestException(Throwable throwable, String msgId, Object... varargs) {
        super(msgId, throwable);
        this.msgId = msgId;
        this.varargs = varargs;
    }
}
