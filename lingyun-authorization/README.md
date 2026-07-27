# lingyun-authorization

认证授权聚合模块。

## 子模块

| 模块 | artifactId | 说明 |
|---|---|---|
| [lingyun-authorization-core](lingyun-authorization-core/) | `lingyun-authorization-core` | 核心契约 |
| [lingyun-authorization-security](lingyun-authorization-security/) | `lingyun-authorization-security-spring-boot-starter` | Spring Security 集成 |

## 依赖层次

```
lingyun-base-core
    ↑
lingyun-authorization-core
    ↑
lingyun-authorization-security-spring-boot-starter  ← + lingyun-base-rsm
```
