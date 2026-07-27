# lingyun-authorization-core

认证授权核心契约——定义 User、Role、SessionManager 等接口，不依赖任何框架。

## 解决了什么问题

把认证授权领域的通用概念（用户、角色、权限、Token）抽象为接口，让不同的框架实现可以基于同一套契约工作。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-authorization-core</artifactId>
    <version>1.0.1</version>
</dependency>
```

依赖：仅 `lingyun-base-core`

## 内容

| 接口/类 | 说明 |
|---|---|
| `User` | 用户身份抽象 |
| `Role` | 角色抽象（权限集合 + 路由 ID） |
| `Credential` | 登录凭证标记 |
| `SessionManager` | Token 签发、解析、移除 |
| `UserToken` | Token 载体 |
| `CertificationChecker<T>` | 认证检查器 |
| `CertificationService` | 认证服务 |
| `ResourceInfo` | API 资源信息 |
| `ResourceInfoService<T>` | 路径 → 资源匹配 |
| `@IsOpen` | 标记端点无需认证 |
