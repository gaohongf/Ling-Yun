# LingYun Authorization Security Spring Boot Starter

> Spring Boot Starter — 认证授权的 Spring Security 集成，引入即自动配置

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-authorization-security-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 自动配置

引入后通过 `AutoConfiguration.imports` 自动加载 `ResourceInfoAutoConfiguration` + `SecurityAutoConfiguration`，无需添加 `@EnableLingYunSecurity`。该注解保留为可选显式声明方式。

## 快速开始

### 1. 实现认证检查器

```java
@Component
public class MyCertificationChecker implements CertificationChecker<MyUser> {
    @Override
    public MyUser authorize(User user) {
        // 检查用户状态 → 加载角色权限 → 构建认证用户
    }
}
```

### 2. 配置

```yaml
lingyun:
  auth:
    filter:
      token-parse: prod     # prod | dev
    custom:
      manager: prod         # prod | dev
```

## 内容

### 核心组件

| 类 | 说明 |
|---|---|
| `SecurityAutoConfiguration` | 自动配置入口 |
| `ResourceInfoAutoConfiguration` | 资源信息服务自动配置 |
| `SecurityFilterChainHelper` | SecurityFilterChain 构建辅助 |
| `CertifiedUser` | Authentication + IdentifiedUser 实现 |
| `ProdAuthorizationManager` | 生产鉴权（5 级优先级链） |
| `DevAuthorizationManager` | 开发鉴权（始终放行） |
| `CustomAccessDeniedHandler` | 403 → RSM 统一响应 |

### 过滤器链

| 类 | 顺序 | 说明 |
|---|---|---|
| `ResourceFilter` | +10 | URI → ResourceInfo |
| `TokenParseFilter` | +20（抽象基类） | Token 解析 |
| `ProdTokenParseFilter` | +20 | Bearer Token → CertifiedUser |
| `DevTokenParseFilter` | +20 | 默认用户 id=2 |

### 鉴权优先级（Prod）

1. errorPath → 放行
2. 无 ResourceInfo → 已认证即放行
3. `@IsOpen` 公开端点 → 放行
4. 用户拥有 resource.id() 权限 → 放行
5. 否则 → 403
