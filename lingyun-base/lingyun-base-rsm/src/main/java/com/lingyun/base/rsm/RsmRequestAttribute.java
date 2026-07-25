package com.lingyun.base.rsm;

import com.lingyun.base.rsm.message.MessageWithParams;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 请求级 RSM 属性存储 — 在单次请求范围内传递消息和响应配置。
 * <p>
 * 底层依赖 Spring {@link RequestAttributes}（线程安全、请求隔离）。
 */
public enum RsmRequestAttribute {
    /** R.msg() 设置的消息 — 优先级高于注解声明的消息 */
    MESSAGE,
    /** 响应头消息 */
    HEADER_MESSAGE,
    /** 验证框架确认的响应消息（如 Bean Validation 消息键） */
    CONFIRMED_RESPONSE_MESSAGE;

    private static final String PREFIX = "rsm_";

    /**
     * 向当前请求域中设置消息键和参数。
     *
     * @param value 消息和参数载体
     */
    public void set(MessageWithParams value) {
        currentAttributes().setAttribute(PREFIX + name(), value, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 向当前请求域中设置响应消息实体。
     *
     * @param value 响应消息实体（通常由验证框架确认后设置）
     */
    public void set(com.lingyun.base.rsm.message.ResponseMessage value) {
        currentAttributes().setAttribute(PREFIX + name(), value, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 从当前请求域中获取已存储的属性值。
     *
     * @param <T> 属性类型
     * @return 已存储的值，若未设置则返回 null
     */
    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) currentAttributes().getAttribute(PREFIX + name(), RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 判断当前请求域中是否已设置该属性。
     *
     * @return true 表示已设置
     */
    public boolean exists() {
        return currentAttributes().getAttribute(PREFIX + name(), RequestAttributes.SCOPE_REQUEST) != null;
    }

    /**
     * 从当前请求域中移除该属性。
     */
    public void remove() {
        currentAttributes().removeAttribute(PREFIX + name(), RequestAttributes.SCOPE_REQUEST);
    }

    private static RequestAttributes currentAttributes() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("No thread-bound request found — are you calling RSM outside a Spring Web request context?");
        }
        return attrs;
    }
}
