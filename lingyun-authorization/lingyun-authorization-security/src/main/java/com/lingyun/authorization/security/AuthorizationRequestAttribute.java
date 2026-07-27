package com.lingyun.authorization.security;

import org.springframework.security.core.Authentication;

import com.lingyun.authorization.core.api.ResourceInfo;
import com.lingyun.authorization.core.entity.CertifiedUser;
import com.lingyun.base.request.CustomRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 认证授权相关的请求属性 — 在过滤器链中通过 HttpServletRequest.setAttribute 存储/读取。
 * <p>
 * 类似于 {@code RsmRequestAttribute}，但支持泛型值类型，且不依赖 Spring RequestContextHolder。
 *
 * @param <T> 属性值类型
 */
public class AuthorizationRequestAttribute<T> extends CustomRequestAttributes<T>{
    
    private static final String PREFIX = "auth_";

    /** 当前请求匹配到的 API 资源信息（由 ResourceFilter 设置） */
    public static final AuthorizationRequestAttribute<ResourceInfo> AUTHORIZATION_RESOURCE_INFO =
            new AuthorizationRequestAttribute<>("AUTHORIZATION_RESOURCE_INFO");

    /** 已认证的用户（由 TokenParseFilter 设置） */
    public static final AuthorizationRequestAttribute<CertifiedUser<Authentication>> AUTHORIZATION_CERTIFIED_USER =
            new AuthorizationRequestAttribute<>("AUTHORIZATION_CERTIFIED_USER");

    /** 请求中的原始 Token 字符串（由 TokenParseFilter 设置） */
    public static final AuthorizationRequestAttribute<String> AUTHORIZATION_TOKEN =
            new AuthorizationRequestAttribute<>("AUTHORIZATION_TOKEN");

    private AuthorizationRequestAttribute(String name) {
        super(PREFIX + name);
    }

    /**
     * 将属性值存入指定的 {@link HttpServletRequest} 中。
     * <p>
     * 底层调用 {@code request.setAttribute(attributeName, value)}，存储的属性键带有
     * {@code "auth_"} 前缀以避免与 Web 容器的其他属性冲突。
     *
     * @param request 目标请求对象
     * @param value   要存储的属性值
     */
    public void set(HttpServletRequest request, T value) {
        request.setAttribute(name, value);
    }

    /**
     * 从指定的 {@link HttpServletRequest} 中读取已存储的属性值。
     * <p>
     * 返回值的类型由声明时的泛型参数决定，调用方无需强制转换。
     * 若此前未调用 {@link #set(HttpServletRequest, Object)}，则返回 {@code null}。
     *
     * @param request 源请求对象
     * @return 属性值，未存储时返回 {@code null}
     */
    @SuppressWarnings("unchecked")
    public T get(HttpServletRequest request) {
        return (T) request.getAttribute(name);
    }
}
