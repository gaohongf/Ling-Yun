package com.lingyun.authorization.ra.mvc;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import com.lingyun.authorization.ra.BranchInfo;

import jakarta.servlet.http.HttpServletRequest;

public class BranchRequestCondition implements RequestCondition<BranchRequestCondition> {

    private final BranchInfo branchInfo;
    private final BranchAuthorizationMatcher branchAuthorizationMatcher;

    public BranchRequestCondition(BranchInfo branchInfo, BranchAuthorizationMatcher matcher) {
        this.branchInfo = branchInfo;
        this.branchAuthorizationMatcher = matcher;
    }

    @Override
    public BranchRequestCondition combine(BranchRequestCondition other) {
        return new BranchRequestCondition(other.branchInfo, other.branchAuthorizationMatcher);
    }

    @Override
    public int compareTo(BranchRequestCondition other, HttpServletRequest request) {
        return Integer.compare(other.branchInfo.getOrder(), this.branchInfo.getOrder());
    }

    @Override
    public BranchRequestCondition getMatchingCondition(HttpServletRequest request) {
        if (branchAuthorizationMatcher.match(branchInfo)) {
            return this;
        }
        return null;
    }

}
