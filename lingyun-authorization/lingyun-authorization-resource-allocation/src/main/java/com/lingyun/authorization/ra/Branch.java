package com.lingyun.authorization.ra;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 端点分支注解——允许多个 Controller 方法共享同一 HTTP 路径，根据用户权限区分命中哪个方法。
 * <p>
 * 同一路径上的多个方法按 {@link #order()} 降序排列，匹配时遍历所有分支，
 * 找到首个用户拥有对应权限（{@code GET:/path#branchName}）的方法执行。
 *
 * <h3>示例</h3>
 * <pre>{@code
 * @Branch(value = "admin", order = 3)
 * @GetMapping("/users")
 * public User adminView() { ... }
 *
 * @Branch(value = "manager", order = 2)
 * @GetMapping("/users")
 * public User managerView() { ... }
 *
 * @Branch(value = "user", order = 1)
 * @GetMapping("/users")
 * public User userView() { ... }
 * }</pre>
 * <p>
 * 用户拥有 {@code GET:/users#manager} 权限时命中 {@code managerView()}，
 * 无分支权限时命中 order 最低的方法。
 *
 * @see com.lingyun.authorization.ra.mvc.BranchRequestMappingHandlerMapping
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Branch {

    /**
     * 分支名称——同一个路径下此值必须唯一。
     *
     * @return 分支名（如 {@code "admin"}）
     */
    String value();

    /**
     * 匹配优先级——数值越大越优先匹配。
     * <p>
     * 用户无任何分支权限时，命中 order 最低的方法作为兜底。
     *
     * @return 优先级，默认 0
     */
    int order() default 0;
}
