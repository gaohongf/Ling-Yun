package com.lingyun.authorization.ra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分支信息 POJO——承载 {@link Branch} 注解的运行时数据。
 *
 * @see Branch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchInfo {

    /** 分支名（对应 {@code @Branch.value()}） */
    private String name;

    /** 匹配优先级（对应 {@code @Branch.order()}，数值越大越优先） */
    private int order;
}
