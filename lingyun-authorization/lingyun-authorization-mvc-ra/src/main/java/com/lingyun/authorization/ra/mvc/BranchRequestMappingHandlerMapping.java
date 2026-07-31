package com.lingyun.authorization.ra.mvc;

import java.lang.reflect.Method;

import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.lingyun.authorization.ra.Branch;
import com.lingyun.authorization.ra.BranchInfo;

public class BranchRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        Branch branch = method.getAnnotation(Branch.class);
        System.out.println(branch);
        return branch != null ? new BranchRequestCondition(new BranchInfo(
                branch.value(),
                branch.order()), new BranchAuthorizationMatcher()) : null;
    }
}
