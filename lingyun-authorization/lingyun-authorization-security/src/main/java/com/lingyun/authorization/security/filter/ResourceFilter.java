package com.lingyun.authorization.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lingyun.authorization.core.api.ResourceInfoService;
import com.lingyun.authorization.security.AuthorizationRequestAttribute;

import java.io.IOException;

/**
 * 资源识别过滤器 — 在每次请求时通过 {@link ResourceInfoService} 查找当前 HTTP 方法和路径
 * 对应的 {@link com.lingyun.authorization.core.api.ResourceInfo}，并将结果存入请求属性中，
 * 供后续的 {@link com.lingyun.authorization.security.CustomAuthorizationManager} 鉴权时使用。
 * <p>
 * 仅当容器中存在 {@link ResourceInfoService} Bean 时才会注册，否则不启用资源级鉴权。
 * 排序在 {@link TokenParseFilter} 之前执行（Order = HIGHEST_PRECEDENCE + 10）。
 */
public class ResourceFilter extends OncePerRequestFilter implements Ordered {

    /**
     * 返回 {@code HIGHEST_PRECEDENCE + 10}，确保在 {@link TokenParseFilter}（+20）
     * 之前执行，以便鉴权管理器同时获取资源信息和用户信息。
     */
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 10;
    }

    private final ResourceInfoService<?> resourceInfoService;
    
    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath = "/error";

    private String favicon = "favicon.ico";
    /**
     * 构造资源过滤器。
     *
     * @param resourceInfoService 用于匹配 HTTP 方法与路径到资源信息的服务
     */
    public ResourceFilter(ResourceInfoService<?> resourceInfoService) {
        this.resourceInfoService = resourceInfoService;
    }

    /**
     * 从请求中提取 HTTP 方法和 URI 路径，通过 {@link ResourceInfoService#optMatchPath(String, String)}
     * 匹配对应的资源信息，并将结果存入 {@link AuthorizationRequestAttribute#AUTHENTICATION_RESOURCE_INFO}。
     * <p>
     * 若未匹配到任何资源，则不设置请求属性，由后续的鉴权管理器决定处理方式。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (favicon.equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }


        resourceInfoService.optMatchPath(request.getMethod(), path)
                .ifPresent(res -> AuthorizationRequestAttribute.AUTHORIZATION_RESOURCE_INFO.set(request, res));
        filterChain.doFilter(request, response);
    }
}
