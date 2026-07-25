package com.lingyun.authorization.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.lingyun.authorization.core.api.ResourceInfoService;
import com.lingyun.authorization.security.filter.ResourceFilter;
import com.lingyun.authorization.security.filter.TokenParseFilter;

/**
 * LingYun 安全自动配置入口。
 * <p>
 * 负责根据配置属性（lingyun.auth.*）和环境变量，自动注册 Token 解析过滤器、鉴权管理器、
 * 资源过滤器、访问拒绝处理器以及 {@link SecurityFilterChainHelper}，实现安全组件的
 * 一键自动装配。
 * </p>
 *
 * @see LingYunSecurityProperties
 * @see SecurityFilterChainHelper
 */
@AutoConfiguration
@Import({
    AuthenticationRsm.class
})
public class SecurityAutoConfiguration {

    /**
     * 注册资源过滤器。
     * <p>仅当容器中存在 {@link ResourceInfoService} Bean 时生效。</p>
     *
     * @param resourceInfoService 资源信息服务
     * @return 资源过滤器实例
     */
    @ConditionalOnBean(ResourceInfoService.class)
    @Bean
    public ResourceFilter resourceFilter(ResourceInfoService<?> resourceInfoService) {
        return new ResourceFilter(resourceInfoService);
    }

    /**
     * 注册默认的访问拒绝处理器。
     * <p>仅当容器中不存在自定义 {@link CustomAccessDeniedHandler} 时生效。</p>
     *
     * @return 默认访问拒绝处理器
     */
    @ConditionalOnMissingBean(CustomAccessDeniedHandler.class)
    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    /**
     * 注册开发环境的 Token 解析过滤器。
     * <p>当配置 {@code lingyun.auth.filter.token-parse=dev} 时生效。</p>
     *
     * @return 开发环境 Token 解析过滤器
     */
    @ConditionalOnProperty(prefix = "lingyun.auth.filter", name = "token-parse", havingValue = "dev")
    @Bean
    public TokenParseFilter devTokenParseFilter() {
        return new DevTokenParseFilter();
    }

    /**
     * 注册生产环境的 Token 解析过滤器。
     * <p>当配置 {@code lingyun.auth.filter.token-parse=prod} 时生效。</p>
     *
     * @return 生产环境 Token 解析过滤器
     */
    @ConditionalOnProperty(prefix = "lingyun.auth.filter", name = "token-parse", havingValue = "prod")
    @Bean
    public TokenParseFilter prodTokenParseFilter() {
        return new ProdTokenParseFilter();
    }

    /**
     * 注册开发环境的鉴权管理器。
     * <p>当配置 {@code lingyun.auth.custom.manager=dev} 时生效，始终放行所有请求。</p>
     *
     * @return 开发环境鉴权管理器
     */
    @ConditionalOnProperty(prefix = "lingyun.auth.custom", name = "manager", havingValue = "dev")
    @Bean
    public CustomAuthorizationManager devAuthorizationManager() {
        return new DevAuthorizationManager();
    }

    /**
     * 注册生产环境的鉴权管理器。
     * <p>当配置 {@code lingyun.auth.custom.manager=prod} 时生效，执行完整的权限校验逻辑。</p>
     *
     * @return 生产环境鉴权管理器
     */
    @ConditionalOnProperty(prefix = "lingyun.auth.custom", name = "manager", havingValue = "prod")
    @Bean
    public CustomAuthorizationManager prodAuthorizationManager() {
        return new ProdAuthorizationManager();
    }

    /**
     * 注册安全过滤器链组装助手。
     * <p>仅当 TokenParseFilter、CustomAccessDeniedHandler、AccessDeniedHandler 和
     * ResourceFilter 四个 Bean 全部就绪时生效，确保所有安全组件都已正确装配。</p>
     *
     * @return 安全过滤器链组装助手
     */
    @ConditionalOnBean({
            TokenParseFilter.class,
            CustomAccessDeniedHandler.class,
            AccessDeniedHandler.class,
            ResourceFilter.class
    })
    @Bean
    public SecurityFilterChainHelper securityFilterChainHelper() {
        return new SecurityFilterChainHelper();
    }

}
