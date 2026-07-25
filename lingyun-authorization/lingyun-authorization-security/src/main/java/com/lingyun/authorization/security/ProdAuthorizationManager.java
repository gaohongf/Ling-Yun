package com.lingyun.authorization.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import com.lingyun.authorization.core.api.ResourceInfo;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 生产环境鉴权管理器 — 根据 ResourceInfo 和 CertifiedUser 决定是否放行。
 * <p>
 * 鉴权逻辑（按优先级）：
 * <ol>
 *   <li>errorPath → 放行</li>
 *   <li>无 ResourceInfo（未配置 ResourceFilter）→ 已认证用户放行</li>
 *   <li>ResourceInfo.isOpen() → 放行（公开端点）</li>
 *   <li>CertifiedUser 拥有 ResourceInfo.id() 对应权限 → 放行</li>
 *   <li>否则 → 拒绝</li>
 * </ol>
 */
public class ProdAuthorizationManager implements CustomAuthorizationManager {

    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath;

    /**
     * 根据请求上下文和用户认证信息执行逐级鉴权检查。
     * <p>
     * 鉴权优先级：error 路径放行 &rarr; 无资源信息时已认证即放行 &rarr;
     * 公开端点放行 &rarr; 检查用户权限 &rarr; 拒绝。
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();

        // 1. error 页面始终放行
        if (Objects.equals(errorPath, request.getRequestURI())) {
            return new AuthorizationDecision(true);
        }

        ResourceInfo resource = AuthorizationRequestAttribute.AUTHORIZATION_RESOURCE_INFO.get(request);

        // 2. 无资源信息 → 未配置 ResourceFilter，已认证用户即放行
        if (resource == null) {
            return new AuthorizationDecision(authentication.get() instanceof CertifiedUser);
        }

        // 3. 公开端点放行
        if (resource.isOpen()) {
            return new AuthorizationDecision(true);
        }

        // 4. 检查用户是否拥有该端点所需的权限
        if (authentication.get() instanceof CertifiedUser user) {
            return new AuthorizationDecision(user.isAuthorized(resource.id()));
        }

        // 5. 拒绝
        return new AuthorizationDecision(false);
    }
}
