package com.lingyun.authorization.ra.mvc;

import java.util.Collection;

import com.lingyun.authorization.core.api.ResourceInfo;
import com.lingyun.authorization.core.entity.CertifiedUser;
import com.lingyun.authorization.core.request.BaseAuthorizationRequestAttribute;
import com.lingyun.authorization.ra.BranchInfo;

/**
 * 分支权限匹配器——检查当前认证用户是否拥有访问指定分支的权限。
 * <p>
 * 权限匹配规则：从当前请求的 {@link ResourceInfo#id()} 构造分支权限标识
 * {@code GET:/path#branchName}，然后检查用户的权限集合中是否包含该标识。
 * <p>
 * 如果分支信息为 {@code null}（方法未标注 {@code @Branch}），直接放行。
 */
public class BranchAuthorizationMatcher {

    /**
     * 判断当前认证用户是否拥有指定分支的访问权限。
     * <p>
     * 权限标识格式：{@code {HTTP方法}:{路径}#{分支名}}。
     * 例如用户拥有 {@code GET:/users#admin} 权限时，可命中 {@code @Branch("admin")} 标注的方法。
     *
     * @param info 分支信息，{@code null} 时直接返回 true（无分支限制）
     * @return true — 有权限或非分支接口；false — 无权限
     */
    public boolean match(BranchInfo info) {
        if (info == null) return true;
        ResourceInfo resourceInfo = BaseAuthorizationRequestAttribute.AUTHORIZATION_RESOURCE_INFO.get();
        String id = resourceInfo.id();
        String branchId = id + "#" + info.getName(); 
        return BaseAuthorizationRequestAttribute.AUTHORIZATION_CERTIFIED_USER.getOpt()
                .map(CertifiedUser::getPermissionIds)
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(branchId::equals);
    }
}
