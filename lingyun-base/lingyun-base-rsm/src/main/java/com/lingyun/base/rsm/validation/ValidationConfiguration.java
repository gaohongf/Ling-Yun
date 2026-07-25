package com.lingyun.base.rsm.validation;

import com.lingyun.base.rsm.message.ResponseMessageService;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.Validation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 验证配置 — 当存在 ResponseMessageService 实现时，注册 DatabaseMessageInterpolator。
 */
@Configuration
@ConditionalOnBean(ResponseMessageService.class)
public class ValidationConfiguration {

    /**
     * 注册数据库驱动的消息插值器。
     * 默认插值器通过 Jakarta Validation API 获取，确保数据库中没有的消息键能正常回退。
     *
     * @param messageService 消息存储服务，用于从数据库加载消息模板
     * @return DatabaseMessageInterpolator 实例
     */
    @Bean
    public MessageInterpolator databaseMessageInterpolator(ResponseMessageService messageService) {
        MessageInterpolator defaultInterpolator = Validation.byDefaultProvider()
                .configure()
                .getDefaultMessageInterpolator();
        return new DatabaseMessageInterpolator(defaultInterpolator, messageService);
    }

    /**
     * 注册 LocalValidatorFactoryBean，注入数据库驱动的消息插值器。
     * Spring Boot 自动配置的 LocalValidatorFactoryBean 会被此 bean 取代，
     * 使得所有验证注解的 message 都能从数据库模板表解析。
     *
     * @param messageService 消息存储服务，用于从数据库加载消息模板
     * @return 配置了 DatabaseMessageInterpolator 的 LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean mvcValidator(ResponseMessageService messageService) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setMessageInterpolator(new DatabaseMessageInterpolator(
                factoryBean.getMessageInterpolator(), messageService));
        return factoryBean;
    }
}
