package com.lingyun.authorization.security;

import com.lingyun.authorization.core.session.CertificationChecker;
import com.lingyun.authorization.security.filter.TokenParseFilter;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * 生产环境 Token 解析过滤器 — 从请求头/ Cookie 中提取 Bearer Token，校验并构建认证信息。
 * <p>
 * 流程：
 * <ol>
 *   <li>提取 Authorization header 或 cookie 中的 token</li>
 *   <li>若 token 不存在，直接放行（由后续 AuthorizationManager 决定是否拒绝）</li>
 *   <li>通过 SessionManager 解析 token → User</li>
 *   <li>通过 CertificationChecker 授权 → CertifiedUser</li>
 *   <li>存入 SecurityContextHolder 和请求属性</li>
 * </ol>
 */
public class ProdTokenParseFilter extends TokenParseFilter {

    @Resource
    private com.lingyun.authorization.core.session.SessionManager sessionManager;
    @Resource
    private CertificationChecker<CertifiedUser> certificationChecker;
    @Resource
    private HandlerExceptionResolver handlerExceptionResolver;

    /**
     * 从请求头 {@code Authorization: Bearer <token>} 或 Cookie {@code token} 中提取 Token，
     * 通过 {@code SessionManager} 解析为 {@code User}，再通过 {@link CertificationChecker} 加载角色和权限
     * 构建 {@link CertifiedUser}，最后存入 {@code SecurityContextHolder} 和请求属性。
     * <p>
     * 无 Token 时直接放行，由后续的 {@link ProdAuthorizationManager} 拒绝非公开请求。
     * Token 解析失败时通过 {@link HandlerExceptionResolver} 返回统一结构的 401 错误响应。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Cookie token = Arrays.stream(cookies)
                        .filter(cookie -> Objects.equals(cookie.getName(), "token"))
                        .findFirst().orElse(null);
                if (token != null) {
                    authorization = "Bearer " + token.getValue();
                }
            }
        }

        // 无 token → 放行（由 AuthorizationManager 拒绝非公开请求）
        if (authorization == null || authorization.isBlank() || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        authorization = authorization.substring(7);
        AuthorizationRequestAttribute.AUTHORIZATION_TOKEN.set(request, authorization);

        try {
            var user = sessionManager.parse(authorization);
            CertifiedUser authorized = certificationChecker.authorize(user);
            AuthorizationRequestAttribute.AUTHORIZATION_CERTIFIED_USER.set(request, authorized);
            SecurityContextHolder.getContext().setAuthentication(authorized);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 若无法识别 token 且端点非公开，则转发到 error 页面
            request.setAttribute("jakarta.servlet.error.status_code", 401);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
