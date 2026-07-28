package com.lingyun.authorization.security;

import org.springframework.security.core.Authentication;

import com.lingyun.authorization.core.api.ResourceInfo;
import com.lingyun.authorization.core.entity.CertifiedUser;
import com.lingyun.authorization.core.request.BaseAuthorizationRequestAttribute;
import com.lingyun.base.request.CustomRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 认证授权请求属性的 Servlet API 存取适配器。
 * <p>
 * 预置属性常量定义在 core 模块的 {@link BaseAuthorizationRequestAttribute} 中，
 * 如需存取，使用静态辅助方法 {@link #setAttr} / {@link #getAttr}，
 * 或将通用常量转为当前类型后调用实例方法。
 *
 * @param <T> 属性值类型
 */
public class AuthorizationRequestAttribute<T> extends CustomRequestAttributes<T> {

    /** 当前请求匹配到的 API 资源信息（由 ResourceFilter 设置） */
    public static final AuthorizationRequestAttribute<ResourceInfo> AUTHORIZATION_RESOURCE_INFO =
            new AuthorizationRequestAttribute<>(BaseAuthorizationRequestAttribute.AUTHORIZATION_RESOURCE_INFO);

    @SuppressWarnings("rawtypes")
    public static final AuthorizationRequestAttribute<CertifiedUser<Authentication>> AUTHORIZATION_CERTIFIED_USER =
            new AuthorizationRequestAttribute(BaseAuthorizationRequestAttribute.AUTHORIZATION_CERTIFIED_USER);

    /** 请求中的原始 Token 字符串（由 TokenParseFilter 设置） */
    public static final AuthorizationRequestAttribute<String> AUTHORIZATION_TOKEN =
            new AuthorizationRequestAttribute<>(BaseAuthorizationRequestAttribute.AUTHORIZATION_TOKEN);

    protected <A extends BaseAuthorizationRequestAttribute<T>>AuthorizationRequestAttribute(A attribute) {
        super(attribute.name);
    }

    /**
     * 通过 Servlet API 写入属性值。
     *
     * @param request 目标请求对象
     * @param value   要存储的属性值
     */
    public void set(HttpServletRequest request, T value) {
        request.setAttribute(name, value);
    }

    /**
     * 通过 Servlet API 读取属性值。
     *
     * @param request 源请求对象
     * @return 属性值，未存储时返回 {@code null}
     */
    public T get(HttpServletRequest request) {
        return (T) request.getAttribute(name);
    }
}
