package com.lingyun.authorization.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lingyun.base.rsm.annotation.EnableRsm;

/**
 * 启用 LingYun Security 自动配置（可选）。
 * <p>
 * 引入此模块后，安全组件会通过 {@code AutoConfiguration.imports} 自动生效，
 * <b>无需标注此注解</b>。仅在需要显式声明依赖时使用。导入的组件：
 * </p>
 * <ul>
 *   <li>{@link ServletMvcResourceAuthorityMappingManager} —— 基于 Servlet MVC 注解扫描的资源-权限映射管理器</li>
 *   <li>{@link ResourceInfoAutoConfiguration} —— 资源信息服务自动配置</li>
 *   <li>{@link SecurityAutoConfiguration} —— 安全组件（Token 过滤器、鉴权管理器等）自动配置</li>
 * </ul>
 * <p>
 * 同时激活 {@link EnableRsm @EnableRsm}，将响应标准化框架（RSM）也一并启用。
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * @SpringBootApplication
 * @EnableLingYunSecurity
 * public class MyApplication {
 *     public static void main(String[] args) {
 *         SpringApplication.run(MyApplication.class, args);
 *     }
 * }
 * }</pre>
 */
@Documented
@Import({
        ServletMvcResourceAuthorityMappingManager.class,
        ResourceInfoAutoConfiguration.class,
        SecurityAutoConfiguration.class
})
@EnableRsm
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableLingYunSecurity {
}
