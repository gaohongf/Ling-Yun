package com.lingyun.base.rsm.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lingyun.base.rsm.annotation.EnableRsm;

/**
 * 启用 RSM + Spring Web MVC 完整自动配置（可选）。
 * <p>
 * 引入 {@code rsm-mvc-spring-boot-starter} 后，MVC 适配会自动生效，<b>无需标注此注解</b>。
 * 仅在需要显式声明依赖或覆盖自动配置时使用。自动注册的组件：
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
