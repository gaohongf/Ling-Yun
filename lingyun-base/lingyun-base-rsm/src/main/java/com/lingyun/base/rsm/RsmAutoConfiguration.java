package com.lingyun.base.rsm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingyun.base.rsm.message.Response;
import com.lingyun.base.rsm.message.ResponseMessageService;

/**
 * RSM 自动配置 — 装配响应包装器执行链的核心组件。
 *
 * <p>
 * 装配逻辑：
 * <ol>
 * <li>{@link AnnotationResponsePackConfiguration} — 加载
 * {@code http.response.packer.annotation.*} 配置属性</li>
 * <li>{@link MessageResponseBuilder} — 默认响应构建器（需要
 * {@link ResponseMessageService} bean）</li>
 * <li>{@link AnnotationPackagingActuator} — 注解驱动的包装执行器</li>
 * <li>{@link ResponsePackagingActuatorManager} — 执行器链管理器（含
 * {@link DefaultResponsePackagingActuator} 兜底）</li>
 * </ol>
 *
 * <p>
 * 扩展点：消费项目可以通过注册自定义的 {@link ResponseBuilder}、{@link ResponsePackagingActuator}
 * 或 {@link ResponsePackagingActuatorManager} bean 来覆盖默认行为。
 */
@AutoConfiguration
public class RsmAutoConfiguration {

    /**
     * 注册注解驱动的响应包装配置属性。
     * <p>
     * 配置前缀为 {@code http.response.packer.annotation}，包含默认消息键、自动包装开关等。
     *
     * @return AnnotationResponsePackConfiguration 实例
     */
    @ConfigurationProperties(prefix = "http.response.packer.annotation")
    @Bean
    public AnnotationResponsePackConfiguration annotationResponsePackConfiguration() {
        return new AnnotationResponsePackConfiguration();
    }

    /**
     * 注册默认响应构建器（需存在 {@link ResponseMessageService}）。
     * <p>
     * 当消费项目未提供自定义 {@link ResponseBuilder} 时自动生效。
     *
     * @param responseMessageService 消息存储服务，用于从数据库解析消息模板
     * @return 基于数据库消息模板的 {@link MessageResponseBuilder}
     */
    @ConditionalOnMissingBean(ResponseBuilder.class)
    @ConditionalOnBean(ResponseMessageService.class)
    @Bean(name = "messageResponseBuilder")
    public ResponseBuilder<Response> responseBuilder(ResponseMessageService responseMessageService) {
        return new MessageResponseBuilder(responseMessageService);
    }

    /**
     * 注册注解驱动的包装执行器。
     * <p>
     * 根据 {@link com.lingyun.base.rsm.annotation.BodyPackSetting @BodyPackSetting}、
     * {@link com.lingyun.base.rsm.annotation.ExecutionSuccess @ExecutionSuccess}、
     * {@link com.lingyun.base.rsm.annotation.ExecutionFailed @ExecutionFailed}
     * 注解决定是否包装以及使用哪条消息。
     *
     * @param configuration   注解响应包装配置
     * @param responseBuilder 响应构建器（由消费项目注入）
     * @return AnnotationPackagingActuator 实例
     */
    @ConditionalOnBean(ResponseBuilder.class)
    @Bean
    public AnnotationPackagingActuator annotationPackagingActuator(
            AnnotationResponsePackConfiguration configuration,
            ResponseBuilder<?> responseBuilder) {
        return new AnnotationPackagingActuator(configuration, responseBuilder);
    }

    /**
     * 注册响应包装执行器链管理器。
     * <p>
     * 按优先级组装执行器链：
     * <ol>
     * <li>{@link AnnotationPackagingActuator} — 注解驱动包装（可选）</li>
     * <li>{@link DefaultResponsePackagingActuator} — 兜底执行器（始终生效）</li>
     * </ol>
     * <p>
     * 消费项目可注册自定义 {@link ResponsePackagingActuatorManager} 完全接管执行器链。
     *
     * @param configuration               注解响应包装配置
     * @param responseBuilder             响应构建器
     * @param errorPackagingActuator      错误包装执行器（可选，可由 rsm-mvc 提供）
     * @param annotationPackagingActuator 注解驱动包装执行器（可选）
     * @return 组装完成的执行器链管理器
     */
    @ConditionalOnMissingBean(ResponsePackagingActuatorManager.class)
    @Bean
    public ResponsePackagingActuatorManager responsePackagingActuatorManager(
            AnnotationResponsePackConfiguration configuration,
            ResponseBuilder<?> responseBuilder,
            @Autowired(required = false) ErrorPackagingActuator errorPackagingActuator,
            @Autowired(required = false) AnnotationPackagingActuator annotationPackagingActuator) {
        List<ResponsePackagingActuator> actuators = new ArrayList<>();
        if (errorPackagingActuator != null) {
            actuators.add(errorPackagingActuator);
        }
        if (annotationPackagingActuator != null) {
            actuators.add(annotationPackagingActuator);
        }
        actuators.add(new DefaultResponsePackagingActuator());
        return new ResponsePackagingActuatorManager(actuators);
    }

    /**
     * 注册 JSON 响应体包装器。
     * <p>由 MVC 适配器（{@code JsonResponseBodyPackerMvcAdapter}）调用，
     * 根据执行器链将 Controller 返回值包装为统一的 RSM 响应体。
     *
     * @param responsePackagingActuatorManager 包装执行器链管理器
     * @param objectMapper                     Jackson ObjectMapper，用于序列化响应体
     * @return JsonResponseBodyPacker 实例
     */
    @Bean
    public JsonResponseBodyPacker jsonResponseBodyPacker(
            ResponsePackagingActuatorManager responsePackagingActuatorManager,
            ObjectMapper objectMapper) {
        return new JsonResponseBodyPacker(responsePackagingActuatorManager, objectMapper);
    }

    /**
     * 注册 RSM 消息加载器（需存在 {@link ResponseMessageService}）。
     * <p>应用启动时扫描所有 {@link RsmManager} 实现类中的 {@code @RsmInfo} 声明，
     * 将消息模板同步到数据库，确保消息表与代码定义一致。
     *
     * @return RsmLoader 实例
     */
    @ConditionalOnBean(ResponseMessageService.class)
    @Bean
    public RsmLoader rsmLoader() {
        return new RsmLoader();
    }

}
