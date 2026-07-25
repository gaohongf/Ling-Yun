package com.lingyun.authorization.core.api;

import java.util.Optional;

/**
 * 资源信息匹配服务 — 根据 HTTP 方法和路径查找对应的 {@link ResourceInfo}。
 * <p>
 * 在认证授权流程中，请求进入后，鉴权过滤器调用此服务将 URL（如 {@code POST /api/users}）
 * 匹配到对应的资源定义，从而获取资源的权限标识和开放状态，继而判断是否需要认证、
 * 当前用户是否有权访问。
 * <p>
 * <b>泛型参数</b>：{@code <T extends ResourceInfo>} 允许具体项目传入自定义的资源信息类型，
 * 携带项目特有的扩展字段（如资源所属模块、资源分组等）。
 * <p>
 * 使用示例：
 * <pre>{@code
 * public class MyResourceService implements ResourceInfoService<ApiResource> {
 *     public ApiResource matchPath(String method, String path) {
 *         // 从数据库或缓存中匹配资源
 *     }
 * }
 * }</pre>
 *
 * @param <T> 资源信息类型，必须实现 {@link ResourceInfo}
 */
public interface ResourceInfoService<T extends ResourceInfo> {

    /**
     * 根据 HTTP 方法和请求路径匹配资源。
     *
     * @param method HTTP 方法（如 {@code "GET"}、{@code "POST"}），不能为 {@code null}
     * @param path   请求路径（如 {@code "/api/users"}），不能为 {@code null}
     * @return 匹配到的资源；未匹配到任何资源时返回 {@code null}
     */
    T matchPath(String method, String path);

    /**
     * 根据 HTTP 方法和请求路径匹配资源，返回 {@link Optional}。
     * <p>
     * 默认实现调用 {@link #matchPath(String, String)} 并用 {@link Optional#ofNullable(Object)} 包装结果。
     * 子类可覆盖此方法以提供更高效的实现。
     *
     * @param method HTTP 方法（如 {@code "GET"}、{@code "POST"}），不能为 {@code null}
     * @param path   请求路径（如 {@code "/api/users"}），不能为 {@code null}
     * @return 匹配到的资源；未匹配到任何资源时返回 {@link Optional#empty()}
     */
    default Optional<T> optMatchPath(String method, String path) {
        return Optional.ofNullable(matchPath(method, path));
    }
}
