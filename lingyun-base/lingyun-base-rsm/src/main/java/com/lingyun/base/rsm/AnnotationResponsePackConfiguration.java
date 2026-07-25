package com.lingyun.base.rsm;

import java.util.ArrayList;
import java.util.List;

/**
 * 注解响应包装的配置属性。
 * <p>
 * 在 application.yml 中以 {@code http.response.packer.annotation} 为前缀配置。
 */
public class AnnotationResponsePackConfiguration {

    /** 是否启用自动注解包装 */
    private boolean auto = true;

    /** 默认成功消息键 */
    private String defaultSuccessMessage = "";

    /** 默认失败消息键 */
    private String defaultFailedMessage = "";

    private List<Class<?>> ignoreHandlerClasses = new ArrayList<>();

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public String getDefaultSuccessMessage() {
        return defaultSuccessMessage;
    }

    public void setDefaultSuccessMessage(String msg) {
        this.defaultSuccessMessage = msg;
    }

    public String getDefaultFailedMessage() {
        return defaultFailedMessage;
    }

    public void setDefaultFailedMessage(String msg) {
        this.defaultFailedMessage = msg;
    }

    public List<Class<?>> getIgnoreHandlerClasses() {
        return ignoreHandlerClasses;
    }

    public void setIgnoreHandlerClasses(List<Class<?>> ignoreHandlerClasses) {
        this.ignoreHandlerClasses = ignoreHandlerClasses;
    }
}
