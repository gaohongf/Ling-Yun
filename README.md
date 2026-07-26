# Ling-Yun

> 可独立复用的 Java 基础设施库

[![Java 17](https://img.shields.io/badge/Java-17-blue)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-red)](https://maven.apache.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-green)](https://spring.io/projects/spring-boot)
[![Version](https://img.shields.io/badge/Version-1.0.1-brightgreen)]()

## 概述

Ling-Yun 是一组可独立复用的 Java 基础设施库，设计原则：**core 定义契约（零框架依赖），上层模块提供框架适配实现**。

所有 starter 模块**引入即自动配置**，无需手动标注 `@Enable*` 注解。

包含两大领域：
- **lingyun-base** — 响应标准化框架（RSM）
- **lingyun-authorization** — 认证授权框架（核心契约 + Spring Security 集成）

## 模块地图

```
Ling-Yun (1.0.1)
├── lingyun-base/
│   ├── lingyun-base-core/                         纯 JDK 核心 —— 零框架依赖
│   └── lingyun-base-rsm/                          响应标准化框架 —— 与 ORM 解耦
├── lingyun-authorization/
│   ├── lingyun-authorization-core/                认证授权契约 —— 零框架依赖
│   └── lingyun-authorization-security/            Spring Security 集成（artifactId 含 -starter 后缀）
├── rsm-mvc-spring-boot-starter/                   RSM 的 MVC 适配（自动加载）
├── rsm-mybatisplus-spring-boot-starter/           RSM 的 MyBatis-Plus 存储（自动加载）
└── rsm-jdbc-spring-boot-starter/                  RSM 的 JDBC 存储（自动加载）
```

## 快速开始

### 引入即用

所有 `-spring-boot-starter` 后缀的模块**无需任何注解**，引入依赖后自动配置生效：

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>rsm-mvc-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

如果还需要认证授权：

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-authorization-security-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

### 可选：显式声明

虽然自动配置已覆盖，以下注解仍可显式使用（适用于需要精确控制配置顺序的场景）：

| 注解 | 说明 |
|---|---|
| `@EnableRsm` | 显式启用 RSM 核心自动配置 |
| `@EnableRsm4Mvc` | 显式启用 RSM + Spring MVC 全自动配置 |
| `@EnableLingYunSecurity` | 显式启用认证授权 + Spring Security 集成 |

### 30 秒体验

引入 rsm-mvc模块
```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>rsm-mvc-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```
创建测试 Controller
```java
@RestController
public class HelloController {

    /**
     * User
     */
    public record User(Integer id, String name) {
    }
    // GET /hello
    // -> {"code":1002,"data":{"id":1,"name":"zhangsan"},"msg":"查询成功","type":"SUCCESS"}
    @ExecutionSuccess(GenericRsm.QUERY_SUCCESS)
    @ExecutionFailed(GenericRsm.QUERY_FAILED)
    @GetMapping("/hello")
    public User getUser() {
        return new User(1, "zhangsan");
    }
    // GET /hello/error
    // -> {"code":1003,"data":null,"msg":"查询失败","type":"ERROR"}
    @ExecutionSuccess(GenericRsm.QUERY_SUCCESS)
    @ExecutionFailed(GenericRsm.QUERY_FAILED)
    @GetMapping("/hello/error")
    public void queryError() {
        throw new RuntimeException();
    }
}

```

## 按需引入

| 场景 | 引入模块 |
|---|---|
| 只要统一响应格式 | `lingyun-base-rsm` |
| + Spring MVC 自动适配 | `rsm-mvc-spring-boot-starter` |
| + MyBatis-Plus 消息存储 | `rsm-mybatisplus-spring-boot-starter` |
| + Spring Data JDBC 消息存储 | `rsm-jdbc-spring-boot-starter` |
| 只要认证授权接口 | `lingyun-authorization-core` |
| + Spring Security 集成 | `lingyun-authorization-security-spring-boot-starter` |

## 各模块详解

### [lingyun-base-core](lingyun-base/lingyun-base-core/) — 纯 JDK 核心

仅 Lombok（compile-time）。提供 `IdentifiedUser`、`CustomRequestAttributes<T>`。

### [lingyun-base-rsm](lingyun-base/lingyun-base-rsm/) — 响应标准化框架

- 统一响应体 `{ code, data, msg, type }` + `R` 工具类
- 声明式消息管理（`@RsmInfo` → 启动时同步）
- 注解驱动包装（`@ExecutionSuccess` / `@ExecutionFailed` / `@NotPack`）
- Jakarta Validation 中文消息插值器
- 默认内存消息存储（无数据库时自动启用 `DefaultResponseMessageServiceImpl`）

### [rsm-mvc-spring-boot-starter](rsm-mvc-spring-boot-starter/) — MVC 适配

- `ResponseBodyAdvice` 自动包装
- ErrorController 错误路径识别
- `@Aspect` 全局异常 → RSM 错误响应

### [rsm-mybatisplus-spring-boot-starter](rsm-mybatisplus-spring-boot-starter/) — MyBatis-Plus 存储

- `MpResponseMessage`（ORM 注解隔离在子类）
- `MybatisResponseMessageService`（组合 Mapper）

### [rsm-jdbc-spring-boot-starter](rsm-jdbc-spring-boot-starter/) — JDBC 存储

- `JdbcResponseMessage`（`@Table` + `@Id` + `Persistable`）
- `JdbcResponseMessageService`

### [lingyun-authorization-core](lingyun-authorization/lingyun-authorization-core/) — 认证契约

- 实体接口：`User`, `Role`, `Credential`
- 会话管理：`SessionManager`, `UserToken`, `CertificationChecker`
- 资源管理：`ResourceInfo`, `ResourceInfoService`, `@IsOpen`

### [lingyun-authorization-security-spring-boot-starter](lingyun-authorization/lingyun-authorization-security/) — Spring Security 集成

- 无状态 Token 认证（Bearer Token → `SessionManager`）
- 过滤器链：`ResourceFilter` → `TokenParseFilter`
- 5 级鉴权优先级链
- Dev / Prod 双模式

## 依赖关系

```
rsm-mybatisplus-spring-boot-starter ──→ lingyun-base-rsm ──→ lingyun-base-core
rsm-jdbc-spring-boot-starter       ──→ lingyun-base-rsm
rsm-mvc-spring-boot-starter        ──→ lingyun-base-rsm

lingyun-authorization-security-spring-boot-starter
  ──→ lingyun-authorization-core ──→ lingyun-base-core
  ──→ lingyun-base-rsm
```

## 配置参考

```yaml
# RSM 配置
http:
  response:
    packer:
      annotation:
        auto: true
        default-success-message: "Generic_execution_success"
        default-failed-message: "Generic_execution_failed"

# 认证授权配置
lingyun:
  auth:
    filter:
      token-parse: prod     # prod | dev
    custom:
      manager: prod         # prod | dev
```

## 版本历史

| 版本 | 日期 | 说明 |
|---|---|---|
| `1.0.1` | 2026-07-26 | 模块 starter 化：引入即自动配置，无需 @Enable 注解 |
| `1.0.0` | 2026-07-26 | 正式版：RSM 框架 + 认证授权框架 |

## 后续规划

- [ ] `lingyun-base-cache` — EnhancedRedisCacheManager + TTL 语法糖
- [ ] `lingyun-base-mail` — EmailService 接口 + 实现
- [ ] `lingyun-base-query` — AbstractQuery + Equals/Like/Range/Sort 查询 DSL

---

**Ling-Yun** © 2026 — Apache 2.0 License
