package com.lingyun.authorization.ra.mvc;

import org.springframework.web.method.HandlerMethod;

import com.lingyun.authorization.core.api.ResourceInfoBuilder;
import com.lingyun.authorization.core.api.SimpleResourceInfo;
import com.lingyun.authorization.ra.Branch;

public class BranchResourceInfoBuilder implements ResourceInfoBuilder<SimpleResourceInfo> {

    @Override
    public SimpleResourceInfo build(String path, HandlerMethod handlerMethod, String requestMethod, boolean open) {
        SimpleResourceInfo resourceInfo = new SimpleResourceInfo();
        Branch branch = handlerMethod.getMethodAnnotation(Branch.class);
        if (branch == null) {
            resourceInfo.setId(requestMethod + ":" + path);
            resourceInfo.setName(handlerMethod.getMethod().getName());
            resourceInfo.setOpen(open);
            return resourceInfo;
        }
        resourceInfo.setId(requestMethod + ":" + path + "#" + branch.value());
        resourceInfo.setName(handlerMethod.getMethod().getName());
        resourceInfo.setOpen(open);
        return resourceInfo;
    }

    @Override
    public String originalResourceId(SimpleResourceInfo info) {
        String id = info.id();
        if (id.lastIndexOf("#") < 0) {
            return id;
        }
        return id.substring(0, id.lastIndexOf("#"));
    }

}
