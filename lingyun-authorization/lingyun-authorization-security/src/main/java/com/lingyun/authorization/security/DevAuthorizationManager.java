package com.lingyun.authorization.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

/**
 * 开发环境鉴权管理器 — 始终放行所有请求。
 */
public class DevAuthorizationManager implements CustomAuthorizationManager {

    /**
     * 开发环境下始终放行所有请求，不做任何权限校验。
     *
     * @return 始终返回 {@code AuthorizationDecision(true)}
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext context) {
        return new AuthorizationDecision(true);
    }
}
