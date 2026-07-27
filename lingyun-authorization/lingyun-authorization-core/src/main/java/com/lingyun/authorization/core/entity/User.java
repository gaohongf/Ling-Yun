package com.lingyun.authorization.core.entity;

import java.io.Serializable;


/**
 * 用户抽象接口 — 认证模块通过此接口获取用户信息，不依赖具体的 User 实体类。
 * <p>
 * 具体项目实现此接口（如 UserEntity），SessionManager 和 CertificationService 均面向此接口编程。
 */
public interface User extends Serializable {

    /**
     * 获取用户唯一标识。
     *
     * @return 用户 ID，不应为 {@code null}
     */
    Serializable getId();

    /**
     * 获取账号启用状态。
     *
     * @return {@code true} 表示已启用；{@code false} 表示已禁用；
     *         {@code null} 表示未设置（实现类可决定是否等同于 {@code true}）
     */
    Boolean getEnable();

    /**
     * 获取账号锁定状态。
     *
     * @return {@code true} 表示已锁定；{@code false} 表示未锁定；
     *         {@code null} 表示未设置（实现类可决定是否等同于 {@code false}）
     */
    Boolean getLocked();
}
