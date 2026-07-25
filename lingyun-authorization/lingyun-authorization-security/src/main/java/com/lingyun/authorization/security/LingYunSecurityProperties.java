package com.lingyun.authorization.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * LingYun 安全配置属性。
 * <p>
 * 绑定以 {@code lingyun.auth} 为前缀的配置项，用于控制过滤器模式（Token 解析方式）
 * 和鉴权管理器模式（生产/开发环境切换），提供类型安全的配置读取。
 * </p>
 *
 * <p>配置示例（application.yml）：</p>
 * <pre>{@code
 * lingyun:
 *   auth:
 *     filter:
 *       token-parse: prod   # prod 或 dev
 *     custom:
 *       manager: prod       # prod 或 dev
 * }</pre>
 */
@Configuration
@ConfigurationProperties(prefix = "lingyun.auth")
public class LingYunSecurityProperties {
    private FilterConfig filter;

    private Custom custom;

    /**
     * 获取过滤器配置。
     *
     * @return 过滤器配置（指定 Token 解析模式）
     */
    public FilterConfig getFilter() {
        return filter;
    }

    /**
     * 设置过滤器配置。
     *
     * @param filter 过滤器配置
     */
    public void setFilter(FilterConfig filter) {
        this.filter = filter;
    }

    /**
     * 获取自定义鉴权相关配置。
     *
     * @return 自定义鉴权配置（指定鉴权管理器模式）
     */
    public Custom getCustom() {
        return custom;
    }

    /**
     * 设置自定义鉴权相关配置。
     *
     * @param custom 自定义鉴权配置
     */
    public void setCustom(Custom custom) {
        this.custom = custom;
    }

    /**
     * 自定义鉴权配置内部类。
     * <p>控制鉴权管理器（AuthorizationManager）的启用模式。</p>
     */
    public class Custom {
        /** 鉴权管理器模式：prod（生产）或 dev（开发）。 */
        private Env manager;

        /**
         * 获取鉴权管理器模式。
         *
         * @return 环境枚举值（prod 或 dev）
         */
        public Env getManager() {
            return manager;
        }

        /**
         * 设置鉴权管理器模式。
         *
         * @param manager 环境枚举值
         */
        public void setManager(Env manager) {
            this.manager = manager;
        }
    }

    /**
     * 过滤器配置内部类。
     * <p>控制 Token 解析过滤器的启用模式。</p>
     */
    public class FilterConfig {
        /** Token 解析模式：prod（生产）或 dev（开发）。 */
        private Env tokenParse;

        /**
         * 获取 Token 解析模式。
         *
         * @return 环境枚举值（prod 或 dev）
         */
        public Env getTokenParse() {
            return tokenParse;
        }

        /**
         * 设置 Token 解析模式。
         *
         * @param tokenParse 环境枚举值
         */
        public void setTokenParse(Env tokenParse) {
            this.tokenParse = tokenParse;
        }
    }

    /**
     * 环境枚举，用于控制安全组件在不同环境下的行为。
     * <ul>
     *   <li>{@code prod} —— 生产环境，启用完整的 Token 解析和权限校验</li>
     *   <li>{@code dev} —— 开发环境，使用默认用户并始终放行</li>
     * </ul>
     */
    public enum Env {
        /** 生产环境。 */
        prod,
        /** 开发环境。 */
        dev
    }
}
