# lingyun-authorization

认证授权聚合模块。

## 子模块

| 模块 | artifactId | 说明 |
|---|---|---|
| [lingyun-authorization-core](lingyun-authorization-core/) | `lingyun-authorization-core` | 核心契约（User, Role, SessionManager 等） |
| [lingyun-authorization-security](lingyun-authorization-security/) | `lingyun-authorization-security-spring-boot-starter` | Spring Security 集成 |
| [lingyun-authorization-resource-allocation](lingyun-authorization-resource-allocation/) | `lingyun-authorization-resource-allocation` | 请求分支控制核心（@Branch） |
| [lingyun-authorization-mvc-ra](lingyun-authorization-mvc-ra/) | `lingyun-authorization-mvc-ra` | 分支控制 MVC 适配 |

## 依赖层次

```
lingyun-base-core
    ↑
lingyun-authorization-core
    ↑
    ├── lingyun-authorization-security-spring-boot-starter  ← + lingyun-base-rsm
    ├── lingyun-authorization-resource-allocation           ← 独立扩展
    │       ↑
    └── lingyun-authorization-mvc-ra                       ← + spring-boot-starter-web
```
