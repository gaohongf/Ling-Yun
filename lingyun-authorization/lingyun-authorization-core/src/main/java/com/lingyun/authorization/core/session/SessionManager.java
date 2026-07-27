package com.lingyun.authorization.core.session;

import com.lingyun.authorization.core.entity.User;

/**
 * 会话管理抽象 — 定义 Token 的签发、解析、移除操作。
 * <p>
 * 具体项目提供实现（如 JWT + Redis、分布式 Session 等）。
 *
 * <h3>登出模板</h3>
 * 登出由消费方在 Controller 中自行实现，从请求中提取 Token 后调用 {@link #remove(User)}：
 * <pre>{@code
 * @PostMapping("/logout")
 * public void logout(HttpServletRequest request) {
 *     String token = request.getHeader("Authorization").substring(7);
 *     User user = sessionManager.parse(token);
 *     if (user != null) {
 *         sessionManager.remove(user);
 *     }
 * }
 * }</pre>
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

}
