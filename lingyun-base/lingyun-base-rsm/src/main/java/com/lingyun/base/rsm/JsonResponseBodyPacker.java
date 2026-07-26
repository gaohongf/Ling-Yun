package com.lingyun.base.rsm;

import java.lang.reflect.Method;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.lingyun.base.rsm.annotation.NotPack;
import com.lingyun.base.rsm.str.RString;

/**
 * JSON 响应体统一包装 — 核心逻辑，不耦合 Spring Web MVC 的 {@code ResponseBodyAdvice}。
 * <p>
 * 内部通过 {@link ResponsePackagingActuatorManager} 执行器链决定是否包装以及如何包装。
 * 使用 Spring Web MVC 的项目需引入 {@code lingyun-base-rsm-mvc} 模块，
 * 其中的 {@code JsonResponseBodyPackerMvcAdapter} 会自动将此类接入 MVC 响应拦截链。
 */
public class JsonResponseBodyPacker {

    private final ResponsePackagingActuatorManager manager;

    /**
     * 构造响应体包装器，注入执行器链管理器。
     *
     * @param manager 响应包装执行器链管理器（Spring 自动注入）
     */
    public JsonResponseBodyPacker(ResponsePackagingActuatorManager manager) {
        this.manager = manager;
    }

    /**
     * 判断当前返回值是否应被包装。
     * <ul>
     * <li>String 转换器不包装（StringHttpMessageConverter 先于 Jackson 执行）</li>
     * <li>非 JSON 转换器不包装</li>
     * <li>带有 {@link com.lingyun.base.rsm.annotation.NotPack @NotPack} 注解的方法不包装</li>
     * </ul>
     */
    public boolean shouldPack(@NonNull MethodParameter returnType,
            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        if (converterType.equals(StringHttpMessageConverter.class))
            return false;
        if (!AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType))
            return false;
        if (returnType.hasMethodAnnotation(NotPack.class))
            return false;
        return true;
    }

    /**
     * 通过执行器链将 Controller 返回值包装为标准响应格式。
     *
     * @param body                  原始返回值
     * @param returnType            方法参数元信息（从中获取 handlerClass / handlerMethod）
     * @param selectedContentType   选中的 content type（当前未使用，预留扩展）
     * @param selectedConverterType 选中的消息转换器类型
     * @param request               请求
     * @param response              响应
     * @return 包装后的响应体
     */
    public Object pack(@Nullable Object body,
            @NonNull MethodParameter parameter,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response) {
        Method method = parameter.getMethod();
        Class<?> handlerClass = parameter.getContainingClass();
        if (method.getReturnType().equals(RString.class) && body instanceof RString rstr) {
            body = rstr.str();
        }
        Object res = manager.findActuator(handlerClass, method).doSuccess(body, method, handlerClass, request,
                response);

        return res;
    }
}
