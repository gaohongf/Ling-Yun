package com.lingyun.base.rsm.mvc;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lingyun.base.rsm.ResponsePackagingActuatorManager;
import com.lingyun.base.rsm.exception.RequestException;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统一失败响应切面 — 捕获 Controller 方法中未处理的异常，转换为 RequestException。
 */
@Aspect
public class UnifiedFailureResponse {

    private static final Logger log = LoggerFactory.getLogger(UnifiedFailureResponse.class);
    private final ResponsePackagingActuatorManager responsePackagingActuatorManager;

    /**
     * 构造统一失败响应切面。
     *
     * @param responsePackagingActuatorManager 执行器链管理器，用于查找合适的异常包装执行器
     */
    public UnifiedFailureResponse(
            ResponsePackagingActuatorManager responsePackagingActuatorManager) {
        this.responsePackagingActuatorManager = responsePackagingActuatorManager;
    }

    /**
     * 拦截所有 RestController 中标注了 {@code @RequestMapping} 及其组合注解的方法抛出的未处理异常。
     * <p>
     * 处理逻辑：
     * <ul>
     *   <li>{@link RequestException} — 直接重新抛出</li>
     *   <li>{@link ConstraintViolationException} — 直接重新抛出（由验证层专门处理）</li>
     *   <li>其他异常 — 记录错误日志后，通过 {@link ResponsePackagingActuatorManager} 查找
     *       匹配的执行器并委托其创建 {@link RequestException} 抛出</li>
     * </ul>
     *
     * @param point     切点信息，用于获取方法签名
     * @param throwable 被抛出的原始异常
     * @return 此方法永远不会正常返回（总是抛出异常）
     * @throws RequestException            最终抛出的包装异常
     * @throws ConstraintViolationException 验证异常直接透传
     */
    @AfterThrowing(value = """
            @within(org.springframework.web.bind.annotation.RestController)
            && (@annotation(org.springframework.web.bind.annotation.GetMapping)
            || @annotation(org.springframework.web.bind.annotation.PostMapping)
            || @annotation(org.springframework.web.bind.annotation.DeleteMapping)
            || @annotation(org.springframework.web.bind.annotation.PatchMapping)
            || @annotation(org.springframework.web.bind.annotation.RequestMapping)
            || @annotation(org.springframework.web.bind.annotation.PutMapping))
            """, throwing = "throwable")
    public Object aroundRequestHandler(JoinPoint point, Throwable throwable) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        if (throwable instanceof RequestException e) {
            throw e;
        } else if (throwable instanceof ConstraintViolationException e) {
            throw e;
        } else {
            log.error("type:{}, message:{}", throwable.getClass(), throwable.getMessage());
            Class<?> handlerClass = method.getDeclaringClass();
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            ServletRequestAttributes attributes  = ((ServletRequestAttributes) requestAttributes);
            throw responsePackagingActuatorManager.findActuator(handlerClass, method).createRequestException(method,
                    handlerClass, 
                    new ServletServerHttpRequest(attributes.getRequest()),
                    new ServletServerHttpResponse(attributes.getResponse()),
                    throwable, new Object[] { throwable.getMessage() });
        }
    }
}
