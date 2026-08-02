package com.lingyun.authorization.ra.mvc;

import org.springframework.web.method.HandlerMethod;

import com.lingyun.authorization.core.api.ResourceInfoBuilder;
import com.lingyun.authorization.core.api.SimpleResourceInfo;
import com.lingyun.authorization.ra.Branch;

/**
 * 分支资源信息构建器——在构建 API 资源信息时将 {@link Branch @Branch} 分支名附加到资源 ID 中。
 * <p>
 * 资源 ID 格式：
 * <ul>
 * <li>无分支：{@code GET:/path}</li>
 * <li>有分支：{@code GET:/path#branchName}</li>
 * </ul>
 * 这使得权限系统可以精确区分同一路径下的不同分支方法。
 *
 * @see ResourceInfoBuilder
 */
public class BranchResourceInfoBuilder implements ResourceInfoBuilder<SimpleResourceInfo> {

    /**
     * 根据 HandlerMethod 构建资源信息，识别 {@link Branch @Branch} 并构造分支资源 ID。
     *
     * @param path          请求路径
     * @param handlerMethod 处理器方法
     * @param requestMethod HTTP 方法（GET/POST 等）
     * @param open          是否为公开端点（@IsOpen）
     * @return 带分支信息的资源描述
     */
    @Override
    public SimpleResourceInfo build(String path, HandlerMethod handlerMethod,
            String requestMethod, boolean open) {
        SimpleResourceInfo resourceInfo = new SimpleResourceInfo();
        Branch branch = handlerMethod.getMethodAnnotation(Branch.class);
        resourceInfo.setName(handlerMethod.getMethod().getName());
        resourceInfo.setOpen(open);
        resourceInfo.setId(branch == null
                ? (requestMethod + ":" + path)
                : (requestMethod + ":" + path + "#" + branch.value()));
        return resourceInfo;
    }

    /**
     * 还原原始资源 ID（去掉分支后缀）。
     *
     * @param info 带分支信息的 ResourceInfo
     * @return 去掉 {@code #branchName} 后的原始 ID
     */
    @Override
    public String originalResourceId(SimpleResourceInfo info) {
        String id = info.id();
        if (id.lastIndexOf("#") < 0) {
            return id;
        }
        return id.substring(0, id.lastIndexOf("#"));
    }
}
