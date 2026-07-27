package com.lingyun.authorization.security;

import com.lingyun.authorization.core.entity.CertifiedUser;
import com.lingyun.authorization.core.entity.User;
import com.lingyun.authorization.core.session.CertificationChecker;
import com.lingyun.authorization.security.filter.TokenParseFilter;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * 开发环境 Token 解析过滤器 — 若无 Token 则使用默认用户身份（id=2），方便本地调试。
 */
public class DevTokenParseFilter extends TokenParseFilter {

    private static final User DEV_USER = new User() {
        @Override public java.io.Serializable getId() { return 2; }
        @Override public Boolean getEnable() { return true; }
        @Override public Boolean getLocked() { return false; }
    };

    @Resource
    private CertificationChecker<CertifiedUser<Authentication>> certificationChecker;
    @Resource
    private HandlerExceptionResolver handlerExceptionResolver;

    /**
     * 开发环境下跳过 Token 解析，直接使用预设的默认用户（id=2）通过 {@link CertificationChecker}
     * 构建 {@link CertifiedUser}，并存入 {@code SecurityContextHolder} 和请求属性，方便本地调试。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            CertifiedUser<Authentication> authorized = certificationChecker.authorize(DEV_USER);
            AuthorizationRequestAttribute.AUTHORIZATION_CERTIFIED_USER.set(request, authorized);
            SecurityContextHolder.getContext().setAuthentication(authorized.adapt());
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            request.setAttribute("jakarta.servlet.error.status_code", 401);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
