# lingyun-authorization-security-spring-boot-starter

Spring Security 集成——无状态 Token 认证、资源级鉴权、Dev/Prod 双模式。

## 解决了什么问题

提供完整的 Spring Security 过滤器链和鉴权管理器，引入即完成安全配置。你只需要实现 `CertificationChecker` 和 `SessionManager`。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-authorization-security-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

依赖：`lingyun-authorization-core` + `lingyun-base-rsm` + `spring-boot-starter-security`

## 内容

| 组件 | 说明 |
|---|---|
| `SecurityAutoConfiguration` | 自动配置入口（过滤器链 + 鉴权管理器） |
| `ResourceInfoAutoConfiguration` | 资源信息服务自动配置 |
| `ProdAuthorizationManager` | 5 级鉴权优先级链 |
| `DevAuthorizationManager` | 开发环境始终放行 |
| `CertifiedUser` | Spring Security Authentication 实现 |
| `ResourceFilter` | URI → ResourceInfo 注入 |
| `ProdTokenParseFilter` | Bearer Token 解析 → CertifiedUser |
| `DevTokenParseFilter` | 默认用户（不校验 Token） |

## 你需要实现的接口

- `CertificationChecker` — 验证用户状态 + 加载角色权限
- `SessionManager` — Token 签发、解析、移除

## 配置

```yaml
lingyun:
  auth:
    filter:
      token-parse: prod     # prod | dev
    custom:
      manager: prod         # prod | dev
```
