package com.lingyun.base.rsm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lingyun.base.rsm.GenericRsm;
import com.lingyun.base.rsm.HttpStatusRsm;
import com.lingyun.base.rsm.RsmAutoConfiguration;
import com.lingyun.base.rsm.validation.BaseValidationRsm;

/**
 * 启用 RSM（响应标准化框架）自动配置的开关注解。
 * <p>
 * 标注在 Spring Boot 启动类或任意 {@code @Configuration} 类上，将通过 {@code @Import}
 * 自动注册以下组件：
 * <ul>
 *   <li>{@link com.lingyun.base.rsm.GenericRsm} — 通用 CRUD 消息定义</li>
 *   <li>{@link com.lingyun.base.rsm.HttpStatusRsm} — HTTP 状态码中文消息映射</li>
 *   <li>{@link com.lingyun.base.rsm.validation.BaseValidationRsm} — Jakarta 验证消息模板</li>
 *   <li>{@link com.lingyun.base.rsm.RsmAutoConfiguration} — RSM 自动配置</li>
 * </ul>
 * 若项目同时使用 Spring Web MVC，建议使用 {@link com.lingyun.base.rsm.mvc.EnableRsm4Mvc} 替代。
 *
 * @see com.lingyun.base.rsm.mvc.EnableRsm4Mvc
 */
@Import({
        GenericRsm.class,
        HttpStatusRsm.class,
        BaseValidationRsm.class,
        RsmAutoConfiguration.class
})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface EnableRsm {
}
