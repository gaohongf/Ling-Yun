package com.lingyun.authorization.security;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.lingyun.authorization.core.api.SimpleResourceInfo;
import com.lingyun.authorization.core.api.SimpleResourceInfoBuilder;
import com.lingyun.authorization.core.api.ResourceAuthorityMappingManager;
import com.lingyun.authorization.core.api.ResourceInfoBuilder;
import com.lingyun.authorization.core.api.ResourceInfoService;

/**
 * 资源信息服务自动配置。
 * <p>
 * 当容器中不存在自定义的 {@link ResourceInfoService} 实现时，自动创建基于注解扫描的
 * 默认实现 {@link AutoResourceInfoServiceImpl}。该实现依赖
 * {@link ResourceAuthorityMappingManager} 从控制器注解（如 {@code @RequestMapping}）
 * 中提取资源信息。
 * </p>
 *
 * <p>若缺少必要的 MVC 依赖会抛出 {@link IllegalStateException} 并给出排查指引。</p>
 */
@AutoConfiguration
public class ResourceInfoAutoConfiguration {

    @ConditionalOnMissingBean(ResourceInfoBuilder.class)
    @Bean
    public SimpleResourceInfoBuilder simpleInfoBuilder(){
        return new SimpleResourceInfoBuilder();
    }

    @ConditionalOnClass(RequestMappingHandlerMapping.class)
    @ConditionalOnBean(RequestMappingHandlerMapping.class)
    @Bean
    public ServletMvcResourceAuthorityMappingManager servletMvcResourceAuthorityMappingManager(
        RequestMappingHandlerMapping mapping, 
        ResourceInfoBuilder resourceInfoBuilder
    ){
        return new ServletMvcResourceAuthorityMappingManager(mapping, resourceInfoBuilder);
    }

    /**
     * 注册默认的资源信息服务。
     * <p>
     * 仅在容器中没有自定义 {@link ResourceInfoService} Bean 时才生效。
     * 需要 {@link ResourceAuthorityMappingManager} 来提供注解扫描能力，
     * 如果缺失则抛出异常并提示用户添加 {@code @EnableLingYunSecurity} 注解
     * 或手动提供自定义实现。
     * </p>
     *
     * @param provider {@link ResourceAuthorityMappingManager} 的延迟注入提供者
     * @return 基于注解自动配置的 ResourceInfoService 实现
     * @throws IllegalStateException 当容器中不存在 ResourceAuthorityMappingManager 时抛出
     */
    @ConditionalOnMissingBean(ResourceInfoService.class)
    @Bean
    public ResourceInfoService<SimpleResourceInfo> resourceInfoService(
            ObjectProvider<ResourceAuthorityMappingManager<SimpleResourceInfo>> provider) {
        ResourceAuthorityMappingManager<SimpleResourceInfo> manager = provider.getIfAvailable();
        if (manager == null) {
            throw new IllegalStateException(
                "未发现自定义的 ResourceInfoService,程序试图默认创建一个根据注解自动配置的资源信息服务,但容器中缺少 ResourceAuthorityMappingManager 的实现。\n" +
                "请确保：\n" +
                "1. 如果需要使用MVC spring-webmvc 依赖\n" +
                "并且在配置类上添加 @EnableLingYunSecurity 以达到自动加载适配mvc的ResourceAuthorityMappingManager Bean的条件\n" +
                "2. 手动提供自定义的 ResourceAuthorityMappingManager 或者 ResourceInfoService Bean。"
            );
        }
        return new AutoResourceInfoServiceImpl(manager);
    }
}
