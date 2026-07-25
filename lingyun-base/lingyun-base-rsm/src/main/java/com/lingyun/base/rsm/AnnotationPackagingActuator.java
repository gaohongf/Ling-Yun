package com.lingyun.base.rsm;

import com.lingyun.base.rsm.annotation.ExecutionFailed;
import com.lingyun.base.rsm.annotation.ExecutionSuccess;
import com.lingyun.base.rsm.annotation.NotPack;
import com.lingyun.base.rsm.exception.RequestException;

import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * 注解驱动的响应包装执行器 — 识别 @BodyPackSetting / @ExecutionSuccess / @ExecutionFailed。
 */
public class AnnotationPackagingActuator implements ResponsePackagingActuator {


    private final ResponseBuilder<?> responseBuilder;
    private final boolean enableAuto;
    private final String defaultSuccess;
    private final String defaultFailed;
    private final List<Class<?>> ignoreHandlerClasses;

    public AnnotationPackagingActuator(
            AnnotationResponsePackConfiguration configuration,
            ResponseBuilder<?> responseBuilder
    ) {
        this.enableAuto = configuration.isAuto();
        this.ignoreHandlerClasses = configuration.getIgnoreHandlerClasses();
        this.defaultSuccess = configuration.getDefaultSuccessMessage();
        this.defaultFailed = configuration.getDefaultFailedMessage();
        this.responseBuilder = responseBuilder;
    }

    @Override
    public boolean supports(Class<?> handlerClass, Method method) {
        if (method.isAnnotationPresent(NotPack.class)) {
            return false;
        }
        if (enableAuto) {
            return !ignoreHandlerClasses.contains(handlerClass);
        }
        return method.isAnnotationPresent(ExecutionFailed.class)
                || method.isAnnotationPresent(ExecutionSuccess.class);
    }

    @Override
    public Object doSuccess(Object body, Method method, Class<?> handlerClass, ServerHttpRequest request, ServerHttpResponse response) {
        ExecutionSuccess success = Optional.ofNullable(method.getAnnotation(ExecutionSuccess.class)).orElse(null);
        if (success != null) {
            response.getHeaders().setContentType(MediaType.parseMediaType(success.type()));
            if (success.packing()) {
                return responseBuilder.build(response, success.value(), body, new Object[0]);
            } else {
                return body;
            }
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return responseBuilder.build(response, defaultSuccess, body, new Object[0]);
    }

    @Override
    public RequestException createRequestException(Method method, Class<?> handlerClass, ServerHttpRequest request, ServerHttpResponse response, Throwable throwable, Object[] varargs) {
        ExecutionFailed failed = Optional.ofNullable(method.getAnnotation(ExecutionFailed.class)).orElse(null);
        if (failed != null) {
            response.getHeaders().setContentType(MediaType.parseMediaType(failed.type()));
            return new RequestException(throwable, failed.value(), varargs);
        }
        return new RequestException(throwable, defaultFailed, varargs);
    }
}
