package com.lingyun.base.rsm.annotation;

import java.lang.annotation.*;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 声明 Controller 方法执行失败时使用的响应消息键。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Documented
@ResponseBody
public @interface ExecutionFailed {

    /**
     * 响应消息键，对应数据库中 {@code response_message} 表的 {@code message_key} 字段。
     *
     * @return 消息键
     */
    String value();

    String type() default MediaType.APPLICATION_JSON_VALUE;

}
