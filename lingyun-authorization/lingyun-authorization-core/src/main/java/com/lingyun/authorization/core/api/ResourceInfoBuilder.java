package com.lingyun.authorization.core.api;

import org.springframework.web.method.HandlerMethod;

public interface ResourceInfoBuilder<T extends ResourceInfo> {

    T build(
            String path, HandlerMethod method, String requestMethod, boolean open
    );

    default String originalResourceId(T info) {
        return info.id();
    }
}
