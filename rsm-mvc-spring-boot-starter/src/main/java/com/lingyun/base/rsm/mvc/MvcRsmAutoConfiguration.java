package com.lingyun.base.rsm.mvc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import com.lingyun.base.rsm.JsonResponseBodyPacker;
import com.lingyun.base.rsm.ResponseBuilder;
import com.lingyun.base.rsm.ResponsePackagingActuatorManager;

/**
 * RSM 的 Spring Web MVC 自动配置类。
 * <p>
 * 引入此模块后，通过 {@code AutoConfiguration.imports} 自动激活，
 * 无需手动添加任何注解。自动注册以下 MVC 层 Bean：
 * <ul>
 *   <li>{@link JsonResponseBodyPackerMvcAdapter} — 将核心包装逻辑接入 ResponseBodyAdvice 链</li>
 *   <li>{@link UnifiedFailureResponse} — AOP 切面，统一捕获并转换未处理异常</li>
 *   <li>{@link MvcErrorPackagingActuator} — Spring Boot ErrorController 的响应包装</li>
 * </ul>
 */
@AutoConfiguration
public class MvcRsmAutoConfiguration {

    /**
     * 注册 {@link JsonResponseBodyPackerMvcAdapter}，将核心响应包装器接入 Spring MVC 的
     * {@link org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice} 拦截链。
     *
     * @param jsonResponseBodyPacker 核心响应包装器（来自 rsm 模块）
     * @param responseBuilder        响应结构构造器
     * @return MVC 适配器实例
     */
    @Bean
    public JsonResponseBodyPackerMvcAdapter jsonResponseBodyPackerMvcAdapter(
            JsonResponseBodyPacker jsonResponseBodyPacker,
            ResponseBuilder<?> responseBuilder) {
        return new JsonResponseBodyPackerMvcAdapter(jsonResponseBodyPacker, responseBuilder);
    }

    /**
     * 注册 {@link UnifiedFailureResponse} AOP 切面，拦截 RestController 中的未处理异常
     * 并将其统一转换为 {@link com.lingyun.base.rsm.exception.RequestException}。
     *
     * @param responsePackagingActuatorManager 执行器链管理器
     * @return 统一失败响应切面实例
     */
    @Bean
    public UnifiedFailureResponse unifiedFailureResponse(
            ResponsePackagingActuatorManager responsePackagingActuatorManager) {
        return new UnifiedFailureResponse(responsePackagingActuatorManager);
    }

    /**
     * 注册 {@link MvcErrorPackagingActuator}，负责处理 Spring Boot {@code ErrorController}
     * 错误页面的响应包装。
     * <p>
     * 仅当容器中存在 {@link ResponseBuilder} Bean 时才生效。
     *
     * @param responseBuilder 响应结构构造器
     * @return MVC 错误包装执行器实例
     */
    @ConditionalOnBean(ResponseBuilder.class)
    @Bean
    public MvcErrorPackagingActuator mvcErrorPackagingActuator(ResponseBuilder<?> responseBuilder){
        return new MvcErrorPackagingActuator(responseBuilder);
    }
}
