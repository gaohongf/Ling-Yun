# LingYun Authorization Core

> 认证授权契约层 —— 零框架依赖

## 定位

`lingyun-authorization-core` 定义认证授权领域的**核心接口契约**，完全与 Spring Security 解耦。上层 `lingyun-authorization-security` 提供 Spring Security 集成实现。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-authorization-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

传递依赖：仅 `lingyun-base-core`

## 内容

### 实体接口（`entity` 包）

| 接口 | 说明 |
|---|---|
| `User` | 用户抽象 — `getId()`, `getEnable()`, `getLocked()` |
| `Role` | 角色抽象 — `getName()`, `getAuthorities()`→`Collection<String>`, `getRouteIds()` |
| `Credential` | 凭证标记接口（如 `EmailAndPassword`、`EmailAndCaptcha`） |

> 设计关键：`Role.getAuthorities()` 返回 `Collection<String>` 而非 Spring Security 的 `GrantedAuthority`，实现与框架解耦。

### 会话管理（`session` 包）

| 接口/类 | 说明 |
|---|---|
| `SessionManager` | Token 的签发、解析、移除、登出 |
| `UserToken` | Token 载体 record（`id`, `serial`） |
| `CertificationChecker<T>` | 认证检查器——验证用户状态、加载角色权限、构建认证用户 |
| `CertificationService` | 认证服务接口 |

### 资源管理（`api` 包）

| 接口 | 说明 |
|---|---|
| `ResourceInfo` | API 资源信息抽象 — `id()`, `isOpen()`, `getName()` |
| `ResourceInfoService<T>` | 路径到资源的匹配服务 — `matchPath()`, `optMatchPath()` |
| `@IsOpen` | 标记端点无需认证（公开 API） |

## 设计原则

- **零框架依赖**：不依赖 Spring Security、Spring Web
- **接口即契约**：具体实现由 `lingyun-authorization-security` 和消费项目提供
- **与 Spring Security 解耦**：`Role.getAuthorities()` 返回 plain string，转换工作交给 `CertifiedUser`
