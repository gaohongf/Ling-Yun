package com.lingyun.authorization.security;

import com.lingyun.base.rsm.exception.RequestException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * 权限拒绝处理器 — 当用户无权限访问时触发，返回 403。
 * <p>
 * 此处仍在 Filter 层，Spring 的全局异常处理器无法主动捕捉此异常，
 * 故而手动调用 {@code HandlerExceptionResolver} 使其统一处理。
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Resource(name = "handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    /**
     * 处理权限拒绝事件——设置响应状态为 403，并通过 {@link HandlerExceptionResolver}
     * 手动将请求转发给全局异常处理器以生成统一结构的错误响应。
     * <p>
     * 由于此方法在 Filter 层执行，Spring MVC 的 {@code @ExceptionHandler}
     * 无法自动捕获此处抛出的异常，因此须手动委托给 {@code HandlerExceptionResolver}。
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(403);
        handlerExceptionResolver.resolveException(request, response, this,
                new RequestException("403"));
    }
}
