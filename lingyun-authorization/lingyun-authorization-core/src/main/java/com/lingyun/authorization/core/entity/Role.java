package com.lingyun.authorization.core.entity;

import java.io.Serializable;
import java.util.Collection;

/**
 * 角色抽象接口 — 定义角色名、API 权限集合和前端路由集合。
 * <p>
 * <b>与 Spring Security 解耦</b>：{@code getAuthorities()} 返回
 * {@code Collection<String>}（纯字符串权限标识），
 * 而非 {@code org.springframework.security.core.GrantedAuthority}。
 * 具体由 {@code CertifiedUser}（security 模块）负责转换为 Spring Security 的权限对象。
 */
public interface Role extends Serializable {

    String ALL_ROUTE = "*";
    String ALL_AUTHORITIES = "*:*";

    /** 角色名（唯一标识） */
    String getName();

    /** 该角色拥有的 API 权限标识集合，如 {@code ["user:create", "order:read"]} */
    Collection<String> getAuthorities();

    /**
     * 该角色拥有的前端路由 ID 集合，如 {@code ["dashboard", "user-mgmt"]}。
     * <p>
     * 路由是扁平结构（树形转换由前端完成）。返回空集合表示该角色没有路由权限。
     * 返回 {@code ["*"]} 表示拥有全部路由。
     */
    default Collection<String> getRouteIds() {
        return java.util.Collections.emptySet();
    }
}
