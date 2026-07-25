package com.lingyun.authorization.security;

import com.lingyun.authorization.core.entity.Role;
import com.lingyun.authorization.core.entity.User;
import com.lingyun.base.user.IdentifiedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 已认证用户 — 同时实现 Spring Security 的 {@link Authentication} 和 LingYun 的 {@link IdentifiedUser}。
 * <p>
 * 在 Token 解析成功后由 {@code CertificationService} 构建，存入 {@code SecurityContextHolder}。
 */
public class CertifiedUser implements Authentication, IdentifiedUser {

    public static final GrantedAuthority ROOT_AUTHORITY = new SimpleGrantedAuthority("*:*");

    private final User user;
    private final Collection<Role> roles;
    private final Collection<GrantedAuthority> authorities;
    private final Collection<String> routeIds;

    public CertifiedUser(User user, Collection<? extends Role> roles) {
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
     * 返回当前用户的权限集合。
     * <p>
     * 权限由所有角色的 {@link Role#getAuthorities()} 汇总后转换为 {@link SimpleGrantedAuthority}。
     * 此转换在构造函数中完成，以实现核心契约层与 Spring Security 的解耦。
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

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
    }

    /** 返回用户 ID 的字符串形式，作为 Spring Security 中的主体名称 */
    @Override
    public String getName() {
        return user.getId().toString();
    }

    // ---- IdentifiedUser ----

    /** 返回用户的序列化 ID，实现 {@link IdentifiedUser} 接口 */
    @Override
    public Serializable getId() {
        return user.getId();
    }

    // ---- 便捷方法 ----

    /** 以字符串形式返回用户 ID，若用户为 {@code null} 则返回 {@code null} */
    public String getStrId() {
        return Optional.ofNullable(user).map(User::getId).map(Object::toString).orElse(null);
    }

    /** 以整数形式返回用户 ID，若无法解析则返回 {@code null} */
    public Integer getIntId() {
        return Optional.ofNullable(getStrId()).map(Integer::parseInt).orElse(null);
    }

    /** 判断当前用户是否拥有指定权限 */
    public boolean isAuthorized(String authority) {
        return isAuthorized(new SimpleGrantedAuthority(authority));
    }

    /**
     * 判断当前用户是否拥有指定权限。
     * <p>
     * 若用户拥有 {@link #ROOT_AUTHORITY}（{@code *:*}），则视为拥有所有权限，直接返回 {@code true}。
     *
     * @param authority 待检查的权限
     * @return 是否拥有该权限
     */
    public boolean isAuthorized(SimpleGrantedAuthority authority) {
        if (authorities.contains(ROOT_AUTHORITY)) {
            return true;
        }
        return authorities.contains(authority);
    }
}
