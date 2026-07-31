package com.lingyun.authorization.ra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchInfo {
    private String name;
    private int order;
}
