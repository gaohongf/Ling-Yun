# LingYun Authorization Security

> 认证授权的 Spring Security 集成实现

## 定位

基于 `lingyun-authorization-core` 的契约接口，提供完整的 Spring Security 集成——包括无状态 Token 认证、资源级权限鉴权、开发/生产双模式切换。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-authorization-security</artifactId>
    <version>1.0.0</version>
</dependency>
```

传递依赖：`lingyun-authorization-core` + `lingyun-base-rsm` + `spring-boot-starter-security` + `spring-boot-starter-web`

## 快速开始

### 1. 启用安全配置

```java
@Configuration
@EnableLingYunSecurity
public class AppConfig {
    // 默认启用 Dev/Prod 模式自动切换
}
```

### 2. 实现认证检查器

```java
@Component
public class MyCertificationChecker implements CertificationChecker<MyUser> {
    @Override
    public MyUser authorize(User user) {
        // 1. 检查用户状态（停用/锁定）
        // 2. 加载角色和权限
        // 3. 构建并返回自定义用户对象
    }
}
```

### 3. 实现资源信息服务

```java
@Component
public class MyResourceInfoService implements ResourceInfoService<MyResourceInfo> {
    @Override
    public MyResourceInfo matchPath(String method, String path) {
        // 匹配请求路径到 API 资源定义
    }
}
```

### 4. 配置属性

```yaml
app:
  env: prod          # dev 或 prod，默认 dev
lingyun:
  security:
    enable-auto-resource-info: true   # 自动扫描 @RequestMapping 构建权限映射
```

## 内容

### 核心组件

| 类 | 说明 |
|---|---|
| `SecurityAutoConfiguration` | Spring Boot 自动配置入口 |
| `SecurityFilterChainHelper` | SecurityFilterChain 构建工具 |
| `CertifiedUser` | Spring Security Authentication + IdentifiedUser 双接口实现 |
| `ProdAuthorizationManager` | 生产环境鉴权管理器（5 级优先级链） |
| `DevAuthorizationManager` | 开发环境鉴权管理器（始终放行） |
| `CustomAccessDeniedHandler` | 403 拒绝访问 → RSM 统一响应 |

### 过滤器链

| 类 | 顺序 | 说明 |
|---|---|---|
| `ResourceFilter` | `+10` | 解析请求 URI → `ResourceInfo`，注入请求属性 |
| `TokenParseFilter` | `+20`（抽象基类） | Token 解析过滤器基类 |
| `ProdTokenParseFilter` | `+20` | 生产环境 Token 解析（Bearer Token → SessionManager → CertifiedUser） |
| `DevTokenParseFilter` | `+20` | 开发环境 Token 解析（默认用户 id=2，不校验 Token） |

### 资源权限映射

| 类 | 说明 |
|---|---|
| `ResourceAuthorityMappingManager<T>` | 权限映射管理器接口 |
| `ServletMvcResourceAuthorityMappingManager` | 基于 Spring MVC `RequestMappingHandlerMapping` 自动构建权限映射 |
| `AutoResourceInfoServiceImpl` | 默认 `ResourceInfoService` 实现（AntPathMatcher 匹配） |
| `ResourceInfoAutoConfiguration` | 资源信息自动配置 |
| `AnnotationResourceInfo` | `ResourceInfo` 的简单 POJO 实现 |

### 请求属性

| 类 | 说明 |
|---|---|
| `AuthorizationRequestAttribute<T>` | 认证相关请求属性存储（泛型、无 Spring RequestContextHolder 依赖） |
| `AuthenticationRsm` | 认证相关消息定义（17+ 个消息键） |

### 注解

| 注解 | 说明 |
|---|---|
| `@EnableLingYunSecurity` | 显式启用安全自动配置 |

## 鉴权优先级（Prod）

```
1. errorPath 路径 → 放行
2. 无 ResourceInfo（未配置资源映射）→ 已认证即放行
3. ResourceInfo.isOpen() → 放行（@IsOpen 公开端点）
4. CertifiedUser 拥有 resource.id() 对应权限 → 放行
5. 否则 → 403 拒绝
```

## 请求属性传递链

```
ResourceFilter
  → AuthorizationRequestAttribute.AUTHORIZATION_RESOURCE_INFO.set(request, resourceInfo)

TokenParseFilter (ProdTokenParseFilter / DevTokenParseFilter)
  → AuthorizationRequestAttribute.AUTHORIZATION_CERTIFIED_USER.set(request, certifiedUser)
  → AuthorizationRequestAttribute.AUTHORIZATION_TOKEN.set(request, rawToken)

ProdAuthorizationManager.check()
  → 读取 AUTHORIZATION_RESOURCE_INFO + AUTHORIZATION_CERTIFIED_USER
  → 执行鉴权逻辑
```

## 架构设计

| 设计点 | 说明 |
|---|---|
| 契约分离 | `authorization-core`（零框架契约）+ `authorization-security`（Spring Security 集成） |
| `Role.getAuthorities()`→`Collection<String>` | 与 Spring Security 的 `GrantedAuthority` 完全解耦 |
| `@IsOpen` 归属 | 从通用模块迁至 `authorization-core.annotation`（归属认证领域） |
| 资源管理 | `ResourceInfo` / `ResourceInfoService` 替代原 API 资源管理类 |
