# Ling-Yun

> 可独立复用的 Java 基础设施库

[![Java 17](https://img.shields.io/badge/Java-17-blue)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-red)](https://maven.apache.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-green)](https://spring.io/projects/spring-boot)
[![Version](https://img.shields.io/badge/Version-1.0.0-brightgreen)]()

## 概述

Ling-Yun 是一组可独立复用的 Java 基础设施库，设计原则：**core 定义契约（零框架依赖），上层模块提供框架适配实现**。

包含两大领域：
- **lingyun-base** — 响应标准化框架（RSM）+ 邮件/缓存/查询（规划中）
- **lingyun-authorization** — 认证授权框架（核心契约 + Spring Security 集成）

## 模块地图

```
Ling-Yun (1.0.0)
├── lingyun-base/
│   ├── lingyun-base-core/          纯 JDK 核心 —— 零框架依赖
│   ├── lingyun-base-rsm/           响应标准化框架 —— 与 ORM 解耦、与 MVC 解耦
│   ├── lingyun-base-rsm-mvc/       RSM 的 Spring Web MVC 适配层（可选）
│   ├── lingyun-base-rsm-mybatis/   RSM 的 MyBatis-Plus 消息存储（可选）
│   └── lingyun-base-rsm-jdbc/      RSM 的 Spring Data JDBC 消息存储（可选）
└── lingyun-authorization/
    ├── lingyun-authorization-core/        认证授权契约 —— 零框架依赖
    └── lingyun-authorization-security/    Spring Security 集成实现
```

## 快速开始

### 一键启用

Ling-Yun 提供快捷注解，一行即可完成全部配置：

```java
@SpringBootApplication
@EnableRsm4Mvc            // 启用 RSM + Spring MVC 自动适配（自动 ResponseBodyAdvice、全局异常包装）
@EnableLingYunSecurity    // 启用认证授权 + Spring Security 集成（过滤器链、鉴权管理器、资源映射）
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

| 快捷注解 | 等价于 | 适用场景 |
|---|---|---|
| `@EnableRsm` | `@Import(RsmAutoConfiguration.class)` | 非 MVC 环境（如 WebFlux），仅需核心响应包装 |
| `@EnableRsm4Mvc` | `@EnableRsm` + `@Import(MvcRsmAutoConfiguration.class)` | Spring Web MVC 项目，需要自动包装 Controller 返回值 |
| `@EnableLingYunSecurity` | `@Import({ServletMvc...Manager.class, ResourceInfoAutoConfiguration.class, SecurityAutoConfiguration.class})` | 需要认证授权 + Spring Security |

### 最小引入

仅需 RSM 响应标准化（不存储消息）：

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 完整引入（RSM + MVC + 认证授权）

```xml
<!-- RSM 核心 -->
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm</artifactId>
    <version>1.0.0</version>
</dependency>
<!-- RSM MVC 适配（自动 ResponseBodyAdvice） -->
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm-mvc</artifactId>
    <version>1.0.0</version>
</dependency>
<!-- RSM 消息存储（MyBatis-Plus / Spring Data JDBC 二选一） -->
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm-mybatis</artifactId>
    <version>1.0.0</version>
</dependency>
<!-- 认证授权 -->
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-authorization-security</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 30 秒体验

```java

@RestController
public class HelloController {
    
    // {"code":200, "msg":"操作成功", "data": "hello world!!!", "type":"SUCCESS"}
    @GetMapping("/hello")
    @ExecutionSuccess(GenericRsm.EXECUTION_SUCCESS)
    @ExecutionFailed(GenericRsm.EXECUTION_FAILED)
    public String hello() {
        return "hello world!!!";
    }
    
}
```

## 各模块详解

### [lingyun-base-core](lingyun-base/lingyun-base-core/) — 纯 JDK 核心

仅依赖 JDK 17 + Lombok（compile-time）。提供：
- `IdentifiedUser` — 用户身份标记接口
- `CustomRequestAttributes<T>` — 请求属性存储抽象基类

### [lingyun-base-rsm](lingyun-base/lingyun-base-rsm/) — 响应标准化框架

依赖 `spring-web`（不含 `spring-webmvc`），与 ORM 完全解耦。提供：
- 统一响应体 `{ code, data, msg, type }`
- `R` 静态工具类（`error()`, `msg()`, `params()`）
- 声明式消息管理（`@RsmInfo` → 数据库同步）
- 注解驱动包装控制（`@ExecutionSuccess`, `@ExecutionFailed`, `@NotPack`）
- 责任链式包装执行器（`ResponsePackagingActuator` 链）
- Jakarta/Hibernate Validation 中文消息插值器
- 启动时消息自动同步（`RsmLoader`）

### [lingyun-base-rsm-mvc](lingyun-base/lingyun-base-rsm-mvc/) — MVC 适配

引入即自动注册：
- `ResponseBodyAdvice` → 自动包装所有 `@ResponseBody` 返回值
- ErrorController 错误路径识别
- `@Aspect` 全局异常捕获 → RSM 错误响应

### [lingyun-base-rsm-mybatis](lingyun-base/lingyun-base-rsm-mybatis/) — MyBatis 存储

- `MpResponseMessage`（ORM 注解隔离在子类）
- `ResponseMessageMapper`（MyBatis-Plus BaseMapper）
- `MybatisResponseMessageService`（组合 Mapper，不继承 ServiceImpl）

### [lingyun-base-rsm-jdbc](lingyun-base/lingyun-base-rsm-jdbc/) — JDBC 存储

- 与 MyBatis 实现平行的验证性方案
- `JdbcResponseMessage`（`@Table` + `@Id` + `Persistable`）
- `JdbcResponseMessageService`

### [lingyun-authorization-core](lingyun-authorization/lingyun-authorization-core/) — 认证契约

零框架依赖。定义：
- 实体接口：`User`, `Role`, `Credential`
- 会话管理：`SessionManager`, `UserToken`
- 资源管理：`ResourceInfo`, `ResourceInfoService`
- 注解：`@IsOpen`

> 关键：`Role.getAuthorities()` 返回 `Collection<String>`，与 Spring Security 的 `GrantedAuthority` 完全解耦。

### [lingyun-authorization-security](lingyun-authorization/lingyun-authorization-security/) — Spring Security 集成

- 无状态 Token 认证（Bearer Token → `SessionManager`）
- 过滤器链：`ResourceFilter`（+10）→ `TokenParseFilter`（+20）
- 资源级权限鉴权（5 级优先级链）
- Dev / Prod 双模式自动切换
- 自动资源权限映射（基于 `@RequestMapping` 扫描）

## 依赖关系图

```
lingyun-base-rsm-mybatis ──→ lingyun-base-rsm ──→ lingyun-base-core
lingyun-base-rsm-jdbc    ──→ lingyun-base-rsm          (纯JDK)
lingyun-base-rsm-mvc     ──→ lingyun-base-rsm

lingyun-authorization-security ──→ lingyun-authorization-core ──→ lingyun-base-core
      (Spring Security)              (零框架契约)
      ──→ lingyun-base-rsm
```

## 按需引入策略

| 场景 | 引入模块 |
|---|---|
| 只要统一响应格式，不存消息 | `lingyun-base-rsm` |
| + Spring MVC 环境 | 额外加 `lingyun-base-rsm-mvc` |
| + 消息存储（MyBatis-Plus） | 额外加 `lingyun-base-rsm-mybatis` |
| + 消息存储（Spring Data JDBC） | 额外加 `lingyun-base-rsm-jdbc` |
| 只要认证授权接口 | `lingyun-authorization-core` |
| + Spring Security 集成 | 额外加 `lingyun-authorization-security` |

## 设计决策

1. **Core 零框架依赖** — `lingyun-base-core` 和 `lingyun-authorization-core` 只依赖 JDK，不引入任何框架
2. **ORM 注解隔离** — `ResponseMessage` 为纯 POJO，`@TableName`/`@Table` 等 ORM 注解仅在子类中声明
3. **MVC 解耦** — RSM 仅依赖 `spring-web`，`ResponseBodyAdvice` 适配在 `rsm-mvc` 模块
4. **组合优于继承** — `MybatisResponseMessageService` 组合 Mapper，避免泛型 diamond 问题
5. **`messageKey` 为主键** — ORM 实体以 `messageKey`（天然主键）而非自增 `code` 作为 `@Id`
6. **`Role` 与 Spring Security 解耦** — `getAuthorities()` 返回 `Collection<String>`，转换工作由 `CertifiedUser` 负责
7. **Dev/Prod 双模式** — 通过 `lingyun.auth.filter.token-parse` 和 `lingyun.auth.custom.manager` 自动切换

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
      token-parse: prod     # prod | dev  —— Token 解析过滤器模式
    custom:
      manager: prod         # prod | dev  —— 鉴权管理器模式
```

## 建表 SQL（消息存储）

```sql
-- RSM 消息表（MyBatis-Plus 和 Spring Data JDBC 通用）
CREATE TABLE response_message (
    code            INT AUTO_INCREMENT,
    message_key     VARCHAR(128) PRIMARY KEY,
    template        VARCHAR(512),
    type            VARCHAR(32),
    response_status INT,
    INDEX idx_type (type)
);
```

## 版本历史

| 版本 | 日期 | 说明 |
|---|---|---|
| `1.0.0` | 2026-07-26 | 正式版：RSM 框架 + 认证授权框架 |

## 后续规划

- [ ] `lingyun-base-cache` — EnhancedRedisCacheManager + TTL 语法糖
- [ ] `lingyun-base-mail` — EmailService 接口 + 实现
- [ ] `lingyun-base-query` — AbstractQuery + Equals/Like/Range/Sort 查询 DSL
- [ ] `lingyun-base-mybatis` — MetaObjectHandler 自动填充

---

**Ling-Yun** © 2026 — Apache 2.0 License
