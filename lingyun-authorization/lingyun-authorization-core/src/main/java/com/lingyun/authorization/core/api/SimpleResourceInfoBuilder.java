package com.lingyun.authorization.core.api;

import org.springframework.web.method.HandlerMethod;

public class SimpleResourceInfoBuilder implements ResourceInfoBuilder<SimpleResourceInfo> {

    @Override
    public SimpleResourceInfo build(String path, HandlerMethod handlerMethod, String requestMethod, boolean open) {
        SimpleResourceInfo resourceInfo = new SimpleResourceInfo();
        resourceInfo.setId(requestMethod + ":" + path);
        resourceInfo.setName(handlerMethod.getMethod().getName());
        resourceInfo.setOpen(open);
        return resourceInfo;
    }

}
