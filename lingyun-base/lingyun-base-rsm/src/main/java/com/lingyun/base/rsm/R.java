package com.lingyun.base.rsm;

import com.lingyun.base.rsm.exception.RequestException;

/**
 * RSM 环境下的请求工具 — 继承核心 R.error()，新增 msg/params 等请求上下文方法。
 * <p>
 * 项目引入 lingyun-base-rsm 后应使用此类而非核心的 com.lingyun.base.R。
 */
public final class R {

    private R() {}

    // ──────────── 继承自核心 R.error() ────────────

    /**
     * 抛出携带消息键和参数的业务请求异常。
     *
     * @param msg     消息键（对应数据库中的 message_key）
     * @param varargs 消息模板中的占位符参数
     * @param <T>     允许在需要返回值的方法中调用（实际不会返回，总是抛出异常）
     * @return 永远不会返回（用于满足编译器）
     */
    public static <T> T error(String msg, Object... varargs) {
        throw new RequestException(msg, varargs);
    }

    /**
     * 抛出携带消息键、参数和原始异常的业务请求异常。
     *
     * @param t       原始异常（作为 cause 保留完整堆栈）
     * @param msg     消息键（对应数据库中的 message_key）
     * @param varargs 消息模板中的占位符参数
     * @param <T>     允许在需要返回值的方法中调用（实际不会返回，总是抛出异常）
     * @return 永远不会返回（用于满足编译器）
     */
    public static <T> T error(Throwable t, String msg, Object... varargs) {
        throw new RequestException(t, msg, varargs);
    }

    // ──────────── RSM 特有：请求级消息覆盖 ────────────

    /** 设置当前请求的响应消息（覆盖 @ExecutionSuccess 的默认消息） */
    public static void msg(String msg, Object... varargs) {
        RsmRequestAttribute.MESSAGE.set(
                com.lingyun.base.rsm.message.MessageWithParams.ofMessage(msg).params(varargs));
    }

    /** 为已设置的消息补充参数 */
    public static void params(Object... varargs) {
        RsmRequestAttribute.MESSAGE.set(
                com.lingyun.base.rsm.message.MessageWithParams.ofParams(varargs));
    }

    /** 设置响应头消息 */
    public static void h_msg(String msg, Object... varargs) {
        RsmRequestAttribute.HEADER_MESSAGE.set(
                com.lingyun.base.rsm.message.MessageWithParams.ofMessage(msg).params(varargs));
    }

    /** 为响应头消息补充参数 */
    public static void h_params(Object... varargs) {
        RsmRequestAttribute.HEADER_MESSAGE.set(
                com.lingyun.base.rsm.message.MessageWithParams.ofParams(varargs));
    }
}
