package com.lingyun.base.rsm;

import java.lang.reflect.Method;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import com.lingyun.base.rsm.exception.RequestException;

/**
 * 默认（兜底）响应包装执行器 — supports() 始终返回 true，确保所有响应都经过包装。
 */
public class DefaultResponsePackagingActuator implements ResponsePackagingActuator {

    /**
     * 兜底执行器总是返回 true，确保未匹配到其他执行器的请求也能被包装。
     *
     * @param request      服务端 HTTP 请求
     * @param response     服务端 HTTP 响应
     * @param handlerClass Controller 类
     * @param handler      Controller 方法
     * @return 始终返回 true
     */
    @Override
    public boolean supports(Class<?> handlerClass, Method method) {
        return true;
    }

    /**
     * 默认的成功处理：直接返回原始 body，由上层统一包装。
     *
     * @param request      服务端 HTTP 请求
     * @param response     服务端 HTTP 响应
     * @param body         原始返回值
     * @param handlerClass Controller 类
     * @param handler      Controller 方法
     * @return 原始 body
     */
    @Override
    public Object doSuccess( Object body,
            Method method,
            Class<?> handlerClass,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        return body;
    }

    /**
     * 默认的失败处理：使用消息键 "500" 创建服务器内部错误响应。
     *
     * @param request      服务端 HTTP 请求
     * @param response     服务端 HTTP 响应
     * @param body         原始异常或失败返回值
     * @param handlerClass Controller 类
     * @param handler      Controller 方法
     * @return 封装后的 RequestException，消息键为 "500"
     */
    @Override
    public RequestException createRequestException(Method method, Class<?> handlerClass, ServerHttpRequest request, ServerHttpResponse response, Throwable throwable, Object[] varargs) {
        if (throwable instanceof Exception ex) {
            return new RequestException(ex, HttpStatusRsm.INTERNAL_SERVER_ERROR);
        }
        return new RequestException(HttpStatusRsm.INTERNAL_SERVER_ERROR);
    }
}
