package com.lingyun.base.rsm.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lingyun.base.rsm.annotation.EnableRsm;

/**
 * 启用 RSM + Spring Web MVC 完整自动配置的开关注解。
 * <p>
 * 标注在 Spring Boot 启动类或任意 {@code @Configuration} 类上。与 {@link com.lingyun.base.rsm.annotation.EnableRsm}
 * 的区别在于额外导入了 {@link MvcRsmAutoConfiguration}，从而自动注册：
 * <ul>
 *   <li>{@link JsonResponseBodyPackerMvcAdapter} — 接入 ResponseBodyAdvice 链</li>
 *   <li>{@link UnifiedFailureResponse} — 异常统一转换 AOP 切面</li>
 *   <li>{@link MvcErrorPackagingActuator} — ErrorController 错误页包装</li>
 * </ul>
 * 仅当项目使用了 Spring Web MVC（spring-boot-starter-web）时才应使用此注解。
 *
 * @see com.lingyun.base.rsm.annotation.EnableRsm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EnableRsm
@Import({ MvcRsmAutoConfiguration.class })
public @interface EnableRsm4Mvc {
}
