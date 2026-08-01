package com.lingyun.authorization.ra.mvc;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import com.lingyun.authorization.ra.BranchInfo;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 分支请求匹配条件——实现 Spring MVC 的 {@link RequestCondition}，将分支权限检查
 * 注入到请求映射的匹配流程中。
 *
 * <h3>匹配流程</h3>
 * <ol>
 *   <li>{@link #combine} — 多个条件合并时保留优先级更高的条件</li>
 *   <li>{@link #compareTo} — 按 {@link BranchInfo#getOrder()} 降序排列（order 越大越优先）</li>
 *   <li>{@link #getMatchingCondition} — 委托 {@link BranchAuthorizationMatcher} 检查用户权限，
 *       匹配成功返回 this，失败返回 null（排除该分支）</li>
 * </ol>
 */
public class BranchRequestCondition implements RequestCondition<BranchRequestCondition> {

    private final BranchInfo branchInfo;
    private final BranchAuthorizationMatcher branchAuthorizationMatcher;

    /**
     * 构造分支条件。
     *
     * @param branchInfo  分支信息（name + order）
     * @param matcher     权限匹配器
     */
    public BranchRequestCondition(BranchInfo branchInfo, BranchAuthorizationMatcher matcher) {
        this.branchInfo = branchInfo;
        this.branchAuthorizationMatcher = matcher;
    }

    /**
     * 合并条件——保留 other 的条件。
     */
    @Override
    public BranchRequestCondition combine(BranchRequestCondition other) {
        return new BranchRequestCondition(other.branchInfo, other.branchAuthorizationMatcher);
    }

    /**
     * 按 order 降序排列——数值大的优先。
     */
    @Override
    public int compareTo(BranchRequestCondition other, HttpServletRequest request) {
        return Integer.compare(other.branchInfo.getOrder(), this.branchInfo.getOrder());
    }

    /**
     * 检查当前请求的用户是否有权限访问此分支。
     *
     * @return 匹配成功返回 this，失败返回 null（排除该分支）
     */
    @Override
    public BranchRequestCondition getMatchingCondition(HttpServletRequest request) {
        if (branchAuthorizationMatcher.match(branchInfo)) {
            return this;
        }
        return null;
    }
}
