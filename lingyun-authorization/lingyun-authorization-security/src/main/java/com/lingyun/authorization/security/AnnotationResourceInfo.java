package com.lingyun.authorization.security;

import com.lingyun.authorization.core.api.ResourceInfo;

import lombok.ToString;

/**
 * 基于注解的 {@link ResourceInfo} 实现，由 {@link ServletMvcResourceAuthorityMappingManager}
 * 在启动时扫描所有 {@code @RequestMapping} 端点自动构建。
 * <p>
 * 每个实例对应一个 HTTP 方法 + URL 模式的组合，记录该端点的唯一标识、显示名称以及是否公开访问。
 */
@ToString
public class AnnotationResourceInfo implements ResourceInfo{

    private String id;
    private boolean open;
    private String name;

    /** 设置资源的唯一标识，格式为 {@code "HTTP方法:URL模式"}（如 {@code "GET:/api/users/{id}"}） */
    public void setId(String id) {
        this.id = id;
    }

    /** 设置资源的显示名称（通常为对应 Controller 方法的名称） */
    public void setName(String name) {
        this.name = name;
    }

    /** 设置该端点是否为公开访问（标记了 {@link com.lingyun.authorization.core.api.annotation.IsOpen @IsOpen} 注解） */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /** 返回资源的唯一标识，格式为 {@code "HTTP方法:URL模式"} */
    @Override
    public String id() {
        return id;
    }

    /** 返回该端点是否为公开访问（无需认证） */
    @Override
    public boolean isOpen() {
        return open;
    }

    /** 返回资源的显示名称 */
    @Override
    public String getName() {
        return name;
    }

}
