package com.lingyun.base.rsm;

import java.lang.reflect.Method;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import com.lingyun.base.rsm.exception.RequestException;

/**
 * 响应包装执行器 — 决定是否以及如何包装 Controller 方法的返回值。
 * <p>
 * 多个实现按优先级链式调用，第一个 {@code supports()} 返回 true 的执行器负责包装。
 */
public interface ResponsePackagingActuator {
    boolean supports(Class<?> handlerClass, Method method);

    Object doSuccess(
            Object body,
            Method method,
            Class<?> handlerClass,
            ServerHttpRequest request,
            ServerHttpResponse response
    );

    RequestException createRequestException(Method method, Class<?> handlerClass, ServerHttpRequest request, ServerHttpResponse response, Throwable throwable, Object[] varargs);
}
