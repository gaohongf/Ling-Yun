package com.lingyun.authorization.core.session;

import com.lingyun.authorization.core.entity.User;

/**
 * 会话管理抽象 — 定义 Token 的签发、解析、移除操作。
 * <p>
 * 具体项目提供实现（如 JWT + Redis、分布式 Session 等）。
 */
public interface SessionManager {

    /**
     * 解析 Token 并还原为 User。
     *
     * @param token Token 字符串，不能为 {@code null}
     * @return 解析出的 User 对象；Token 无效、过期或用户不存在时返回 {@code null}
     */
    User parse(String token);

    /**
     * 为给定 User 签发 Token。
     *
     * @param user 要签发 Token 的用户，不能为 {@code null}
     * @return 签发的 Token 字符串，不会为 {@code null}
     */
    String issue(User user);

    /**
     * 移除指定用户的所有活跃 Token（强制下线）。
     *
     * @param user 要移除 Token 的用户，不能为 {@code null}
     */
    void remove(User user);

    /**
     * 登出当前会话（移除当前请求上下文关联的 Token）。
     * <p>
     * 实现类通常从当前请求中获取 Token 进行注销，适用于"退出登录"场景。
     */
    void logout();
}
