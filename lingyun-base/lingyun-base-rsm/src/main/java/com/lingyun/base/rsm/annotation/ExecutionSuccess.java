package com.lingyun.base.rsm.annotation;

import java.lang.annotation.*;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 声明 Controller 方法执行成功时使用的响应消息键。
 * <p>
 * 标注在方法上时，方法正常返回后使用指定的消息键构建响应体；
 * 标注在类上时，该类中所有方法成功时默认使用该消息键（方法级注解优先）。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Documented
@ResponseBody
public @interface ExecutionSuccess {

    /**
     * 响应消息键，对应数据库中 {@code response_message} 表的 {@code message_key} 字段。
     *
     * @return 消息键
     */
    String value();

    String type() default MediaType.APPLICATION_JSON_VALUE;

    boolean packing() default true;

}
