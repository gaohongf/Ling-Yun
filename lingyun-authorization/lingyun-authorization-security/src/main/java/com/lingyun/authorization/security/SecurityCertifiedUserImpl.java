package com.lingyun.authorization.security;

import com.lingyun.authorization.core.entity.CertifiedUser;
import com.lingyun.authorization.core.entity.Role;
import com.lingyun.authorization.core.entity.User;
import com.lingyun.base.user.IdentifiedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * 已认证用户 — 同时实现 Spring Security 的 {@link Authentication} 和 LingYun 的
 * {@link IdentifiedUser}。
 * <p>
 * 在 Token 解析成功后由 {@code CertificationService} 构建，存入
 * {@code SecurityContextHolder}。
 */
public class SecurityCertifiedUserImpl implements Authentication, CertifiedUser<Authentication> {

    public static final GrantedAuthority ROOT_AUTHORITY = new SimpleGrantedAuthority("*:*");

    private final User user;
    private final Collection<Role> roles;
    private final Collection<GrantedAuthority> authorities;
    private final Collection<String> routeIds;

    /**
     * 构造已认证用户——从原始用户和角色集合中提取权限和路由。
     *
     * @param user  原始用户实体
     * @param roles 用户拥有的角色集合
     */
    public SecurityCertifiedUserImpl(User user, Collection<? extends Role> roles) {
        this.user = user;
        this.roles = new HashSet<>(roles);
        this.authorities = roles.stream()
                .map(Role::getAuthorities)
                .flatMap(Collection::stream)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        this.routeIds = roles.stream()
                .map(Role::getRouteIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /** 返回当前用户拥有的角色集合 */
    public Collection<Role> getRoles() {
        return roles;
    }

    /** 从所有角色中汇总的前端路由 ID 集合。返回空集表示无权访问任何路由；包含 {@code "*"} 表示拥有全部路由。 */
    public Collection<String> getRouteIds() {
        return routeIds;
    }

    // ---- Authentication ----

    /**
     * 始终返回 {@code null}——本认证方案基于 Token，不在服务端存储用户凭证。
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /** 返回底层的 {@link User} 实体对象 */
    @Override
    public User getDetails() {
        return user;
    }

    /**
     * 始终返回 {@code null}——用户标识通过 {@link #getName()} 和 {@link #getId()} 获取。
     */
    @Override
    public Object getPrincipal() {
        return null;
    }

    /** 当底层 {@link User} 不为 {@code null} 时表示已认证 */
    @Override
    public boolean isAuthenticated() {
        return user != null;
    }

    /**
     * 空操作——{@code CertifiedUser} 构造后即为不可变对象，认证状态由底层 {@link User} 决定，
     * 不允许外部修改。
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("已经认证通过的用户实例无法更新授权状态");
    }

    /** 返回用户 ID 的字符串形式，作为 Spring Security 中的主体名称 */
    @Override
    public String getName() {
        return user.getId().toString();
    }

    // ---- IdentifiedUser ----
    /**
     * 判断当前用户是否拥有指定权限。
     * <p>
     * 若用户拥有 {@link #ROOT_AUTHORITY}（{@code *:*}），则视为拥有所有权限，直接返回 {@code true}。
     *
     * @param authority 待检查的权限
     * @return 是否拥有该权限
     */
    public boolean isAuthorized(SimpleGrantedAuthority authority) {
        return isAuthorized(authority.getAuthority());
    }

    /**
     * 返回 Spring Security 格式的权限集合。
     * <p>从角色中提取为 {@link SimpleGrantedAuthority}，由 Spring Security 进行权限校验。
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 返回权限标识字符串集合。
     * <p>从 {@link GrantedAuthority} 中提取 authority 字符串，用于 {@code isAuthorized(String)} 比较。
     */
    @Override
    public Collection<String> getPermissionIds() {
        return authorities.stream().map(GrantedAuthority::getAuthority).toList();
    }

    /**
     * 适配为 {@link Authentication}——返回自身，因为本类同时实现了 {@code Authentication}。
     */
    @Override
    public SecurityCertifiedUserImpl adapt() {
        return this;
    }
}
