package com.lingyun.base.rsm;

import com.lingyun.base.rsm.message.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServerHttpResponse;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 默认响应构造器 — 从数据库解析消息模板，构建 {@link Response}。
 */
public class MessageResponseBuilder implements ResponseBuilder<Response> {

    private final ResponseMessageService messageService;

    /**
     * 构造响应构造器。
     *
     * @param messageService 消息存储服务接口（用于查询消息模板）
     */
    public MessageResponseBuilder(ResponseMessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 从数据库查询消息模板，使用 {@link MessageFormat} 格式化占位符参数。
     *
     * @param msg     消息键
     * @param varargs 模板占位符参数
     * @return 格式化后的消息文本，若消息键不存在则返回空字符串
     */
    @Override
    public String buildMessage(String msg, Object[] varargs) {
        return messageService.findOptByMessageKey(msg)
                .map(ResponseMessage::getTemplate)
                .map(template -> MessageFormat.format(template, varargs))
                .orElse("");
    }

    /**
     * 构建完整的 {@link Response} 响应体。
     * <ul>
     *   <li>从 {@link RsmRequestAttribute#HEADER_MESSAGE} 读取响应头消息并设置 X-Status-Message 头</li>
     *   <li>从数据库查询消息模板，若未找到则回退到 {@link HttpStatusRsm#OK}</li>
     *   <li>若请求中通过 {@code R.msg()} 设置了消息，则优先级高于注解声明的消息</li>
     * </ul>
     *
     * @param response 服务端 HTTP 响应
     * @param msg      消息键
     * @param data     响应数据体
     * @param varargs  消息模板占位符参数
     * @return 完整的 Response 对象
     */
    @Override
    public Response build(ServerHttpResponse response, String msg, Object data, Object[] varargs) {
        // 响应头消息
        if (RsmRequestAttribute.HEADER_MESSAGE.exists()) {
            MessageWithParams params = RsmRequestAttribute.HEADER_MESSAGE.get();
            response.getHeaders().add("X-Status-Message",
                    URLEncoder.encode(buildMessage(params.getMessage(), params.getParams()), Charset.defaultCharset()));
        }

        // 优先使用验证框架确认的消息
        ResponseMessage message = Optional.ofNullable(
                        (ResponseMessage) RsmRequestAttribute.CONFIRMED_RESPONSE_MESSAGE.get())
                .or(() -> Optional.ofNullable(messageService.findByMessageKey(msg)))
                .orElseGet(() -> messageService.findByMessageKey(HttpStatusRsm.OK));

        response.setStatusCode(HttpStatusCode.valueOf(message.getResponseStatus()));
        return createResponse(message, () -> varargs, data);
    }

    /**
     * 将 {@link ResponseMessage} 转换为 {@link Response} 响应体。
     * <p>
     * 若请求中通过 {@code R.msg()} 设置了消息，则优先使用请求级消息而非模板消息。
     *
     * @param message 数据库中的响应消息实体
     * @param params  消息模板占位符参数
     * @param data    响应数据体
     * @return 填充完整的 Response 对象
     */
    private Response createResponse(ResponseMessage message, Supplier<Object[]> params, Object data) {
        Response rsp = new Response();
        rsp.setCode(message.getCode());
        rsp.setType(parseType(message.getType()));

        // R.msg() 设置的消息优先级高于注解声明的消息
        if (RsmRequestAttribute.MESSAGE.exists()) {
            MessageWithParams mwp = RsmRequestAttribute.MESSAGE.get();
            rsp.setMsg(MessageFormat.format(mwp.getMessage(), mwp.getParams()));
        } else {
            rsp.setMsg(MessageFormat.format(message.getTemplate(), params.get()));
        }

        rsp.setData(data);
        return rsp;
    }

    /**
     * 将数据库中的 type 字符串解析为 {@link ResponseType} 枚举。
     *
     * @param type 类型字符串（如 "SUCCESS"、"WARN"、"INFO"、"ERROR"）
     * @return 对应的 ResponseType 枚举值，解析失败时返回 SUCCESS
     */
    private static ResponseType parseType(String type) {
        try {
            return type != null ? ResponseType.valueOf(type) : ResponseType.SUCCESS;
        } catch (IllegalArgumentException e) {
            return ResponseType.SUCCESS;
        }
    }
}
