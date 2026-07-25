package com.lingyun.base.user;

import java.io.Serializable;

/**
 * 已认证用户的身份标记接口。
 * <p>
 * 具体项目实现此接口（如 UserEntity），认证模块通过此接口获取用户 ID，
 * 不依赖具体的 User 实体类。
 */
public interface IdentifiedUser {

    /**
     * 获取当前认证用户的唯一标识。
     *
     * @return 用户唯一标识（通常为 {@link Long} 类型的用户 ID 或 {@link String} 类型的 UUID），
     *         不应返回 {@code null}
     */
    Serializable getId();
}
