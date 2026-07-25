package com.lingyun.base.rsm.validation;

import com.lingyun.base.rsm.R;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一验证异常适配 — 将三种标准的 Jakarta/Spring 验证异常转换为 RSM 格式的错误消息。
 */
@RestControllerAdvice
public class Validation2UnifyMessageErrorAdapter {

    /**
     * 处理 {@code @Valid} 标注的请求体参数验证失败异常。
     * 提取第一个字段错误消息作为响应消息键，由 RSM 框架进行消息模板解析。
     *
     * @param e MethodArgumentNotValidException 异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handle(MethodArgumentNotValidException e) {
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            R.msg(error.getDefaultMessage());
            break;
        }
        R.error(e, e.getObjectName());
    }

    /**
     * 处理 GET 请求中简单 POJO 参数绑定失败异常。
     * 提取第一个字段错误消息作为响应消息键。
     *
     * @param e BindException 异常
     */
    @ExceptionHandler(BindException.class)
    public void handle(BindException e) {
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            R.msg(error.getDefaultMessage());
            break;
        }
        R.error(e, e.getObjectName());
    }

    /**
     * 处理方法级约束参数验证失败异常（如 {@code @NotNull}、{@code @Size} 加在参数上）。
     * 提取第一个约束违例消息作为响应消息键。
     *
     * @param e ConstraintViolationException 异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public void handle(ConstraintViolationException e) {
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            R.msg(violation.getMessage());
            break;
        }
        R.error(e, "");
    }
}
