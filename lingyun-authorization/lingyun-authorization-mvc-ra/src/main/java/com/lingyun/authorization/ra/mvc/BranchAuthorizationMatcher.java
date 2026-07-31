package com.lingyun.authorization.ra.mvc;

import java.util.Collection;

import com.lingyun.authorization.core.api.ResourceInfo;
import com.lingyun.authorization.core.entity.CertifiedUser;
import com.lingyun.authorization.core.request.BaseAuthorizationRequestAttribute;
import com.lingyun.authorization.ra.BranchInfo;

public class BranchAuthorizationMatcher {
    public boolean match(BranchInfo info) {
        if (info == null) return true;
        ResourceInfo resourceInfo = BaseAuthorizationRequestAttribute.AUTHORIZATION_RESOURCE_INFO.get();
        String id = resourceInfo.id(); //GET:/a/b/c
        String branchId = id + "#" + info.getName(); //GET:/a/b/c#admin
        return BaseAuthorizationRequestAttribute.AUTHORIZATION_CERTIFIED_USER.getOpt()
                .map(CertifiedUser::getPermissionIds)
                .stream()
                .peek(System.out::println)
                .flatMap(Collection::stream)
                .anyMatch(branchId::equals);
    };
}
