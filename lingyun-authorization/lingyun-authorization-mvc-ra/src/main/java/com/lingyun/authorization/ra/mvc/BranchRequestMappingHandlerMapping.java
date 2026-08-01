package com.lingyun.authorization.ra.mvc;

import java.lang.reflect.Method;

import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.lingyun.authorization.ra.Branch;
import com.lingyun.authorization.ra.BranchInfo;

/**
 * 分支出请求映射处理器——替代 Spring MVC 默认的 {@link RequestMappingHandlerMapping}。
 * <p>
 * 扫描 Controller 方法上的 {@link Branch @Branch} 注解，为带分支标注的方法
 * 创建 {@link BranchRequestCondition}，由 Spring MVC 在多个候选方法中根据条件匹配结果
 * 选择最终命中的方法。
 *
 * <p>通过 {@link MvcRaAutoConfiguration} 自动注册。
 */
public class BranchRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    /**
     * 为标注了 {@link Branch @Branch} 的方法创建自定义匹配条件。
     *
     * @param method 处理器方法
     * @return BranchRequestCondition，无 @Branch 时返回 null
     */
    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        Branch branch = method.getAnnotation(Branch.class);
        return branch != null ? new BranchRequestCondition(new BranchInfo(
                branch.value(),
                branch.order()), new BranchAuthorizationMatcher()) : null;
    }
}
