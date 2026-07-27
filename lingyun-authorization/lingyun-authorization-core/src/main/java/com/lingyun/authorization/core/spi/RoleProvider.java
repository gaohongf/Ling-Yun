package com.lingyun.authorization.core.spi;

import java.util.List;

import com.lingyun.authorization.core.entity.Role;
import com.lingyun.authorization.core.entity.User;

/**
 * 角色数据提供方——消费项目实现此接口以返回用户的角色列表。
 * <p>
 * 属于 SPI 扩展点：框架在认证时调用此接口获取角色和权限数据，
 * 不同项目可以有不同的数据来源（数据库、缓存、外部服务等）。
 */
public interface RoleProvider {

    /**
     * 获取指定用户拥有的角色列表。
     *
     * @param user 待查询的用户实体
     * @return 角色列表，无角色时返回空列表
     */
    List<Role> getUserRoles(User user);
}
