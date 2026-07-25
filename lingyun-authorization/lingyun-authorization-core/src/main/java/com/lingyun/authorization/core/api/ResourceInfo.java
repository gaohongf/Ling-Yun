package com.lingyun.authorization.core.api;

/**
 * 资源信息接口 — 定义 API 端点（资源）的元数据契约。
 * <p>
 * 在认证授权流程中，"资源"指代一个具体的 API 端点（如 {@code POST /api/users}）。
 * 鉴权管理器通过此接口获取资源的权限标识，判断当前用户是否有权访问。
 * <p>
 * 具体项目实现此接口，并通过 {@link ResourceInfoService} 对外提供资源匹配能力。
 * 资源有两种类型：
 * <ul>
 *   <li><b>开放资源</b>（{@code isOpen() == true}）：无需认证，任何请求均可访问</li>
 *   <li><b>受保护资源</b>（{@code isOpen() == false}）：需认证且用户拥有对应权限才能访问</li>
 * </ul>
 */
public interface ResourceInfo {

    /** 权限标识字符串，如 {@code "user:create"} */
    String id();

    /** 是否公开端点（无需认证即可访问） */
    boolean isOpen();

    /**
     * 资源名称（人类可读的描述性名称）。
     * <p>
     * 用于日志记录、审计和错误提示等场景，方便开发者识别具体是哪个端点。
     *
     * @return 资源名称，如 {@code "创建用户"}，不可为 {@code null}
     */
    String getName();
}
