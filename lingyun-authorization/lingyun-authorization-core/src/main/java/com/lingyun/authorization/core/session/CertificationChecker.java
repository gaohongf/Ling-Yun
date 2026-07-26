package com.lingyun.authorization.core.session;

import com.lingyun.authorization.core.entity.User;
import com.lingyun.base.user.IdentifiedUser;

/**
 * 认证检查器 — 消费项目实现此接口，负责：
 * <ol>
 *   <li>验证用户状态（是否停用、锁定）</li>
 *   <li>查询用户角色和权限</li>
 *   <li>构建 {@link CertifiedUser}</li>
 * </ol>
 * <p>
 */
public interface CertificationChecker<T extends IdentifiedUser> {

    /**
     * 对用户进行授权——检查用户状态、加载角色权限、构建 CertifiedUser。
     *
     * @param user 已通过 Token 解析验证身份的原始用户
     * @return 携带角色和权限的已认证用户
     */
    T authorize(User user);
}
