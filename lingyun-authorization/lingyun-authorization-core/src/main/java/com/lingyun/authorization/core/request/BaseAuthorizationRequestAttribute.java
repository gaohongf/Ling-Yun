package com.lingyun.authorization.core.request;

import com.lingyun.authorization.core.api.ResourceInfo;
import com.lingyun.authorization.core.entity.CertifiedUser;
import com.lingyun.base.request.CustomRequestAttributes;

/**
 * 认证授权请求属性存储的抽象基类——不依赖任何 Web 框架。
 * <p>
 * 子类负责具体的属性存取方式（如通过 {@code HttpServletRequest.setAttribute} 或
 * Spring 的 {@code RequestContextHolder}）。预置的属性常量可被不同安全框架实现共用。
 *
 * @param <T> 属性值类型
 */
public class BaseAuthorizationRequestAttribute<T> extends CustomRequestAttributes<T>{

    protected static final String PREFIX = "AUTH_";

    /** 当前请求匹配到的 API 资源信息（由 ResourceFilter 设置） */
    public static final BaseAuthorizationRequestAttribute<ResourceInfo> AUTHORIZATION_RESOURCE_INFO =
            new BaseAuthorizationRequestAttribute<>("AUTHORIZATION_RESOURCE_INFO");

    /** 已认证的用户（由 TokenParseFilter 设置） */
    public static final BaseAuthorizationRequestAttribute<CertifiedUser<?>> AUTHORIZATION_CERTIFIED_USER =
            new BaseAuthorizationRequestAttribute<>("AUTHORIZATION_CERTIFIED_USER");

    /** 请求中的原始 Token 字符串（由 TokenParseFilter 设置） */
    public static final BaseAuthorizationRequestAttribute<String> AUTHORIZATION_TOKEN =
            new BaseAuthorizationRequestAttribute<>("AUTHORIZATION_TOKEN");

    /**
     * 构造实例，自动添加 {@code "auth_"} 前缀。
     *
     * @param name 属性名（不含前缀）
     */
    protected BaseAuthorizationRequestAttribute(String name) {
        super(PREFIX + name);
    }
}
