package com.lingyun.authorization.core.entity;

import com.lingyun.base.user.IdentifiedUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * 已认证用户抽象——不依赖任何安全框架的 {@link IdentifiedUser} 扩展。
 * <p>
 * 泛型参数 {@code T} 表示适配后的目标类型（如 Spring Security 的 {@code Authentication}），
 * 由 {@link #adapt()} 方法转换，使不同安全框架的实现可以各自适配。
 */
public interface CertifiedUser<T> extends IdentifiedUser {

    /** 返回当前用户拥有的角色集合 */
    Collection<Role> getRoles();

    /**
     * 从所有角色汇总的前端路由 ID 集合。空集表示无权访问任何路由；
     * 包含 {@link Role#ALL_ROUTE 超级角色路由}（{@value Role#ALL_ROUTE}）表示拥有全部路由。
     */
    Collection<String> getRouteIds();

    /**
     * 返回当前用户的权限集合。
     */
    Collection<String> getPermissionIds();

    /** 返回底层的 {@link User} 实体对象 */
    public User getDetails();

    // ---- IdentifiedUser ----

    /** 返回用户的序列化 ID，实现 {@link IdentifiedUser} 接口 */
    @Override
    default Serializable getId() {
        return Optional.ofNullable(getDetails()).map(User::getId).orElse(null);
    }

    // ---- 便捷方法 ----

    /** 以字符串形式返回用户 ID，若用户为 {@code null} 则返回 {@code null} */
    default String getStrId() {
        return Optional.ofNullable(getId())
                .map(Object::toString)
                .orElse(null);
    }

    /** 以整数形式返回用户 ID，若无法解析则返回 {@code null} */
    default Integer getIntId() {
        return Optional.ofNullable(getStrId()).map(Integer::parseInt).orElse(null);
    }

    /** 判断当前用户是否拥有指定权限 */
    default boolean isAuthorized(String authority) {
        Collection<String> authorities = getPermissionIds();
        if (authorities.contains(Role.ALL_AUTHORITIES)) {
            return true;
        }
        return authorities.contains(authority);
    }

    /**
     * 将当前实例适配为目标类型 {@code T}。
     * <p>例如 Spring Security 实现中，{@code T = Authentication}，该方法返回 {@code this}。
     *
     * @return 适配后的目标类型实例
     */
    T adapt();
}
