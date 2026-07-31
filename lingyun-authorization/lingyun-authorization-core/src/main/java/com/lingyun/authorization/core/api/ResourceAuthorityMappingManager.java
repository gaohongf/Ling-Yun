package com.lingyun.authorization.core.api;

import java.util.Collection;
import java.util.Map;


/**
 * 适配更多web服务器
 * ResourceMappingManager
 */
public interface ResourceAuthorityMappingManager<T extends ResourceInfo> {
    /**
     * 构建权限映射表。
     * <p>
     * 返回的 Map 中 Key 格式为 {@code "HTTP方法:URL模式"}（如 {@code "GET:/api/users/{id}"}），
     * Value 为对应的资源信息对象。
     *
     * @return HTTP 方法 + URL 模式 → 资源信息的映射表
     */
    Map<String, T> buildAuthorityMap();

    /**
     * 获取全部资源信息
     */
    default Collection<T> getAllResourceInfo(){
        return buildAuthorityMap().values();
    }
    // /**
    //  * 获取资源的显示名称，默认返回 {@link HandlerMethod} 对应的方法名。
    //  * <p>
    //  * 如果需要自定义名称来源（如通过自定义注解 {@code @Name("users")}），
    //  * 可以实现这个接口并重写此方法。
    //  *
    //  * @param method 处理器方法
    //  * @return 资源的显示名称
    //  */
    // String getResourceName(HandlerMethod method);

    ResourceInfoBuilder<T> builder();

}
