package com.lingyun.authorization.security;

import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 * 自定义鉴权管理器接口 — 消费项目提供实现以接入 Spring Security 的请求鉴权链。
 */
public interface CustomAuthorizationManager extends AuthorizationManager<RequestAuthorizationContext> {
}
