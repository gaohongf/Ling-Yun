package com.lingyun.base.rsm.mvc;

import com.lingyun.base.rsm.JsonResponseBodyPacker;
import com.lingyun.base.rsm.ResponseBuilder;
import com.lingyun.base.rsm.exception.RequestException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * {@link JsonResponseBodyPacker} 的 Spring Web MVC 适配器。
 * <p>
 * 通过 {@link ResponseBodyAdvice} 机制将核心包装逻辑接入 MVC 的响应拦截链。
 * 引入 {@code lingyun-base-rsm-mvc} 模块后自动生效，无需额外配置。
 */
@ControllerAdvice
public class JsonResponseBodyPackerMvcAdapter implements ResponseBodyAdvice<Object> {

    private final JsonResponseBodyPacker packer;
    private final ResponseBuilder<?> responseBuilder;

    /**
     * 通过构造注入 {@link JsonResponseBodyPacker}，组合而非继承。
     *
     * @param packer 核心响应包装器
     */
    public JsonResponseBodyPackerMvcAdapter(JsonResponseBodyPacker packer, ResponseBuilder<?> responseBuilder) {
        this.packer = packer;
        this.responseBuilder = responseBuilder;
    }

    /**
     * 判断是否需要包装当前返回值，委托给 {@link JsonResponseBodyPacker#shouldPack}。
     *
     * @param returnType   控制器方法返回类型
     * @param converterType 消息转换器类型
     * @return 是否需要包装
     */
    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return packer.shouldPack(returnType, converterType);
    }

    /**
     * 响应体写出前进行包装处理，委托给 {@link JsonResponseBodyPacker#pack}。
     *
     * @param body                 原始响应体（可为 null）
     * @param returnType           控制器方法返回类型
     * @param selectedContentType  选定的内容类型
     * @param selectedConverterType 选定的消息转换器类型
     * @param request              HTTP 请求
     * @param response             HTTP 响应
     * @return 包装后的响应体
     */
    @Override
    public Object beforeBodyWrite(@Nullable Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        return packer.pack(body, returnType, selectedContentType, selectedConverterType, request, response);
    }


    /**
     * 全局 {@link RequestException} 异常处理器，将业务异常转换为统一的错误响应体。
     * <p>
     * 通过 {@link ResponseBuilder} 构建包含 {@code msgId} 和参数的错误响应结构，
     * 确保 REST API 在抛出 RequestException 时返回一致格式的错误 JSON。
     *
     * @param e        业务请求异常，携带消息键和参数
     * @param response HTTP 响应对象，用于构建 ServerHttpResponse
     * @return 由 ResponseBuilder 构建的错误响应体（JSON 格式）
     */
    @ResponseBody
    @ExceptionHandler(RequestException.class)
    public Object handleApiException(RequestException e, HttpServletResponse response) {
        return responseBuilder.build(new ServletServerHttpResponse(response), e.getMsgId(), null, e.getVarargs());
    }
}
