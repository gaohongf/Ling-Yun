package com.lingyun.authorization.security;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.lingyun.authorization.core.api.ResourceAuthorityMappingManager;
import com.lingyun.authorization.core.api.ResourceInfoBuilder;
import com.lingyun.authorization.core.api.SimpleResourceInfo;
import com.lingyun.authorization.core.api.annotation.IsOpen;

/**
 * 基于 Spring MVC 的 {@link ResourceAuthorityMappingManager} 实现——
 * 通过 {@link RequestMappingHandlerMapping} 在启动时一次性扫描所有 Controller 的
 * {@code @RequestMapping} 映射，构建 HTTP 方法 + URL 模式到
 * {@link SimpleResourceInfo} 的权限映射表。
 * <p>
 * 仅当容器中存在 {@link RequestMappingHandlerMapping} Bean（即项目引入了 Spring Web
 * MVC）时才会注册。
 */
public class ServletMvcResourceAuthorityMappingManager
        implements ResourceAuthorityMappingManager<SimpleResourceInfo> {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final ResourceInfoBuilder<SimpleResourceInfo> builder;

    /**
     * 构造 MVC 资源权限映射管理器。
     *
     * @param requestMappingHandlerMapping Spring MVC 的请求映射注册表，包含所有已注册的
     *                                     HandlerMethod
     */
    public ServletMvcResourceAuthorityMappingManager(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            ResourceInfoBuilder<SimpleResourceInfo> builder) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.builder = builder;
    }

    /**
     * 遍历所有已注册的 {@link HandlerMethod}，将 HTTP 请求方法与 URL 模式排列组合，
     * 构建 {@code "GET:/api/users/{id}"} 格式的 Key → {@link AnnotationResourceInfo}
     * 的映射表。
     * <p>
     * 资源是否公开由目标方法上是否标记了 {@link IsOpen @IsOpen} 注解决定。
     *
     * @return 权限映射表
     */
    @Override
    public Map<String, SimpleResourceInfo> buildAuthorityMap() {
        Map<String, SimpleResourceInfo> map = new ConcurrentHashMap<>();
        requestMappingHandlerMapping.getHandlerMethods().forEach((info, handlerMethod) -> {
            boolean isOpen = handlerMethod.hasMethodAnnotation(IsOpen.class);
            Set<String> directPaths = info.getPatternValues();
            Set<RequestMethod> requestMethods = info.getMethodsCondition().getMethods();
            // 将路径和请求方法排列组合
            for (RequestMethod requestMethod : requestMethods) {
                for (String path : directPaths) {
                    SimpleResourceInfo resourceInfo = builder()
                            .build(path, handlerMethod, requestMethod.name(), isOpen);
                    map.put(resourceInfo.id(), resourceInfo);
                }
            }
        });
        return map;
    }

    @Override
    public ResourceInfoBuilder<SimpleResourceInfo> builder() {
        return builder;
    }
}
