package com.lingyun.base.rsm.mvc;

import com.lingyun.base.rsm.exception.RequestException;
import com.lingyun.base.rsm.ErrorPackagingActuator;
import com.lingyun.base.rsm.ResponseBuilder;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * 错误页包装执行器 — 处理 Spring Boot ErrorController 的响应。
 * <p>
 * 仅当项目引入了 Spring Web MVC（spring-boot-starter-web）时才生效，
 * 因此放在 {@code lingyun-base-rsm-mvc} 模块中。
 */
public class MvcErrorPackagingActuator implements ErrorPackagingActuator {

    private final ResponseBuilder<?> responseBuilder;

    /**
     * 构造 MVC 错误包装执行器。
     *
     * @param responseBuilder 响应结构构造器，用于构建符合项目风格的错误响应体
     */
    public MvcErrorPackagingActuator(ResponseBuilder<?> responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    /**
     * 仅当 handler 是 {@link ErrorController} 实例时介入处理。
     *
     * @param request      HTTP 请求
     * @param response     HTTP 响应
     * @param handlerClass handler 的原始类
     * @param handler      实际的 handler 实例
     * @return true — 当 handler 是 ErrorController 实例
     */
    @Override
    public boolean supports(Class<?> handlerClass,Method method) {
        return ErrorController.class.isAssignableFrom(handlerClass);
    }

    /**
     * 错误页的成功包装——直接返回原始 body，不做额外包装。
     *
     * @param request      HTTP 请求
     * @param response     HTTP 响应
     * @param body         原始响应体
     * @param handlerClass handler 的原始类
     * @param handler      实际的 handler 实例
     * @return 原始 body 不做修改
     */
    @Override
    public Object doSuccess(Object body,
            Method method,
            Class<?> handlerClass,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body instanceof Map<?, ?> map) {
            Object status = map.get("status");
            if (status != null) {
                return responseBuilder.build(response, status.toString(), null, new Object[] { 
                    map.get("error") 
                });
            } else {
                return body;
            }
        } else {
            return body;
        }
    }

    /**
     * 将错误页异常转换为 {@link RequestException}，使用 HTTP 500 作为默认消息键。
     *
     * @param method       handler 方法
     * @param handlerClass handler 所属类
     * @param request      HTTP 请求
     * @param response     HTTP 响应
     * @param throwable    原始异常
     * @param varargs      消息参数
     * @return 包装后的 RequestException，消息键为 "500"
     */
    @Override
    public RequestException createRequestException(Method method, Class<?> handlerClass, ServerHttpRequest request,ServerHttpResponse response, Throwable throwable, Object[] varargs) {
        return new RequestException(throwable, "500", throwable.getMessage());
    }
}
