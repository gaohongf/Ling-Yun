package com.lingyun.base.rsm.annotation;

import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明一个响应消息键，其模板和 HTTP 状态码将被 {@link com.lingyun.base.rsm.RsmLoader} 同步到数据库。
 *
 * <p>使用示例：
 * <pre>{@code
 * public static final String MY_MSG = "My_msg";
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RsmInfo {

    /**
     * 消息模板字符串，可包含 {@link java.text.MessageFormat} 格式的占位符。
     * <p>例如：{@code "用户 {0} 登录成功"}
     *
     * @return 消息模板
     */
    String template();

    /**
     * 该消息对应的 HTTP 状态码。
     *
     * @return HTTP 状态码，默认 {@link org.springframework.http.HttpStatus#OK}
     */
    HttpStatus status();
}
