package com.lingyun.base.rsm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个 Controller 方法或类退出响应自动包装。
 * <p>
 * 当方法返回的已经是标准响应体、或需要返回非标准格式（如文件流）时使用。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface NotPack {
}
