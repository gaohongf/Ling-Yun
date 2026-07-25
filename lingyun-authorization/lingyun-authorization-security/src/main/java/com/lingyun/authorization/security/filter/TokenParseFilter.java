package com.lingyun.authorization.security.filter;

import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Token 解析过滤器抽象基类。
 * <p>
 * 子类（ProdTokenParseFilter / DevTokenParseFilter）实现具体的 Token 提取、校验、用户认证逻辑。
 * 实现 {@link Ordered} 以在 SecurityConfig 的 addFilterBefore 链中正确排序。
 */
public abstract class TokenParseFilter extends OncePerRequestFilter implements Ordered {

    /**
     * 返回 {@code HIGHEST_PRECEDENCE + 20}，保证在 {@link ResourceFilter}（+10）
     * 之后执行，使资源信息先于用户认证信息注入请求上下文，供鉴权管理器同时使用两者。
     */
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 20;
    }
}
