package com.lingyun.authorization.security;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.lingyun.authorization.core.entity.CertifiedUser;
import com.lingyun.authorization.core.entity.Role;
import com.lingyun.authorization.core.entity.User;
import com.lingyun.authorization.core.session.CertificationChecker;
import com.lingyun.authorization.core.spi.RoleProvider;

/**
 * {@link CertificationChecker} 的 Spring Security 默认实现。
 * <p>
 * 通过 {@link RoleProvider} 加载角色和权限数据，构建 {@link SecurityCertifiedUserImpl}
 * 作为 Spring Security 的 {@link Authentication} 实例。
 */
public class SecurityCertificationChecker implements CertificationChecker<CertifiedUser<Authentication>> {

    private final RoleProvider roleProvider;

    /**
     * 构造检查器。
     *
     * @param roleProvider 角色数据提供方
     */
    public SecurityCertificationChecker(RoleProvider roleProvider) {
        this.roleProvider = roleProvider;
    }

    /**
     * 认证用户——加载角色权限数据并构建 {@link SecurityCertifiedUserImpl}。
     *
     * @param user 已通过 Token 解析的原始用户
     * @return 携带角色和权限的 Spring Security 认证用户
     */
    @Override
    public SecurityCertifiedUserImpl authorize(User user) {
        List<Role> userRoles = roleProvider.getUserRoles(user);
        return new SecurityCertifiedUserImpl(user, userRoles);
    }

}
