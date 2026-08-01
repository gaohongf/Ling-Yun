package com.lingyun.authorization.ra.mvc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 请求分支控制（RA）的 MVC 适配自动配置。
 * <p>
 * 注册两个核心组件：
 * <ul>
 *   <li>{@link BranchResourceInfoBuilder} — 构建带分支信息的资源 ID</li>
 *   <li>{@link BranchRequestMappingHandlerMapping} — 替换默认的 {@link RequestMappingHandlerMapping}，
 *       使 {@code @Branch} 注解生效</li>
 * </ul>
 *
 * <p>引入此模块即自动生效，无需手动标注注解。
 */
@AutoConfiguration
public class MvcRaAutoConfiguration {

    /**
     * 注册分支资源信息构建器，将 {@code @Branch} 信息写入资源 ID。
     */
    @Bean
    public BranchResourceInfoBuilder branchResourceInfoBuilder() {
        return new BranchResourceInfoBuilder();
    }

    /**
     * 替换 Spring MVC 默认的 RequestMappingHandlerMapping 为分支感知版。
     */
    @Bean
    public WebMvcRegistrations webMvcRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new BranchRequestMappingHandlerMapping();
            }
        };
    }
}
