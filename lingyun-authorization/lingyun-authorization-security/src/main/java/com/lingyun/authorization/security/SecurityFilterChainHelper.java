package com.lingyun.authorization.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import com.lingyun.authorization.security.filter.ResourceFilter;
import com.lingyun.authorization.security.filter.TokenParseFilter;

/**
 * 安全过滤器链组装助手。
 * <p>
 * 通过 Setter 注入的方式收集 Token 解析过滤器、鉴权管理器、资源过滤器和访问拒绝处理器，
 * 然后在 {@link #configure(HttpSecurity)} 中统一组装 Spring Security 过滤器链，
 * 配置无状态会话、访问拒绝处理和过滤器顺序。
 * </p>
 *
 * <p>典型使用方式：</p>
 * <pre>{@code
 * @Bean
 * SecurityFilterChain securityFilterChain(HttpSecurity security, SecurityFilterChainHelper helper) throws Exception {
 *     return helper.configure(security).build();
 * }
 * }</pre>
 */
public class SecurityFilterChainHelper {
    private ResourceFilter resourceFilter;
    private TokenParseFilter tokenParseFilter;
    private CustomAuthorizationManager customAuthorizationManager;
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * 注入 Token 解析过滤器。
     *
     * @param tokenParseFilter Token 解析过滤器实例
     */
    @Autowired
    public void setTokenParseFilter(TokenParseFilter tokenParseFilter) {
        this.tokenParseFilter = tokenParseFilter;
    }

    /**
     * 注入自定义鉴权管理器。
     *
     * @param customAuthorizationManager 鉴权管理器实例
     */
    @Autowired
    public void setCustomAuthorizationManager(CustomAuthorizationManager customAuthorizationManager) {
        this.customAuthorizationManager = customAuthorizationManager;
    }

    /**
     * 注入资源过滤器。
     *
     * @param resourceFilter 资源过滤器实例
     */
    @Autowired
    public void setResourceFilter(ResourceFilter resourceFilter) {
        this.resourceFilter = resourceFilter;
    }

    /**
     * 注入自定义访问拒绝处理器。
     *
     * @param customAccessDeniedHandler 访问拒绝处理器实例
     */
    @Autowired
    public void setCustomAccessDeniedHandler(CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    /**
     * 组装 HttpSecurity，配置安全过滤器链。
     * <p>
     * 依次配置：
     * <ul>
     *   <li>异常处理 —— 注册自定义访问拒绝处理器</li>
     *   <li>请求授权 —— 所有请求交由鉴权管理器处理</li>
     *   <li>会话管理 —— 无状态会话，适用于 RESTful API</li>
     *   <li>过滤器插入 —— 将资源过滤器和 Token 解析过滤器插入 AuthorizationFilter 之前</li>
     * </ul>
     * </p>
     *
     * @param security Spring Security 的 HttpSecurity 配置对象
     * @return 配置完成后的 HttpSecurity，供调用方继续 .build() 生成 SecurityFilterChain
     * @throws Exception 配置过程中可能抛出的异常
     */
    public HttpSecurity configure(HttpSecurity security) throws Exception {
        return security.exceptionHandling(configurer -> configurer.accessDeniedHandler(customAccessDeniedHandler))
                .authorizeHttpRequests(registry -> registry.anyRequest().access(customAuthorizationManager))
                .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(resourceFilter, AuthorizationFilter.class)
                .addFilterBefore(tokenParseFilter, AuthorizationFilter.class);
    }
}
