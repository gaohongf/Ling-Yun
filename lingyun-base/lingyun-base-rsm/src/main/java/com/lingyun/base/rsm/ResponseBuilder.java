package com.lingyun.base.rsm;

import org.springframework.http.server.ServerHttpResponse;

import java.io.Serializable;

/**
 * 响应构造器 — 将消息键 + 数据包装为项目自定义的响应对象。
 *
 * @param <T> 响应对象类型（如 {@link com.lingyun.base.rsm.message.Response}）
 */
public interface ResponseBuilder<T extends Serializable> {
    /**
     * 将消息键解析为最终展示的文本。
     *
     * @param msg     消息键（对应数据库中的 message_key）
     * @param varargs 消息模板中的占位符参数
     * @return 格式化后的消息文本
     */
    String buildMessage(String msg, Object[] varargs);

    /**
     * 构建完整的响应对象。
     *
     * @param response 服务端 HTTP 响应（可用于设置状态码和响应头）
     * @param msg      消息键
     * @param data     响应数据体
     * @param varargs  消息模板中的占位符参数
     * @return 构建完成的响应对象
     */
    T build(ServerHttpResponse response, String msg, Object data, Object[] varargs);
}
