# CLAUDE.md

This file provides guidance to Claude Code when working in this repository.

## Project Overview

**LingYun** — 可独立复用的 Java 基础设施库。设计原则：**core 定义契约（零框架依赖），上层模块提供框架适配实现**。

- **语言**: Java 17
- **构建**: Maven 多模块
- **版本**: 1.0.0

## 顶层模块

```
Ling-Yun/
├── pom.xml                            ← 所有模块的公共父 POM（版本管理）
├── lingyun-base/                      ← 基础设施库（RSM + cache/mail/query 规划中）
└── lingyun-authorization/             ← 认证授权框架（core 契约 + Spring Security 集成）
```

## 快捷注解

| 注解 | 说明 | 适用场景 |
|---|---|---|
| `@EnableRsm` | 启用 RSM 核心自动配置 | 非 MVC 环境 |
| `@EnableRsm4Mvc` | 启用 RSM + Spring MVC 全自动配置 | Spring Web MVC 项目 |
| `@EnableLingYunSecurity` | 启用认证授权 + Spring Security 集成 | 需要认证授权的项目 |

> classpath 包含对应模块时自动配置默认生效，使用注解可显式声明。

## lingyun-base Module Structure

```
lingyun-base/                        ← 聚合 POM (groupId=com.lingyun)
├── lingyun-base-core/               ← 纯 JDK，零框架依赖
├── lingyun-base-rsm/                ← 响应标准化框架（需 Spring Web，不含 MVC）
├── lingyun-base-rsm-mvc/            ← RSM 的 Spring Web MVC 适配层（可选）
├── lingyun-base-rsm-mybatis/        ← RSM 的 MyBatis-Plus 存储实现（可选）
└── lingyun-base-rsm-jdbc/           ← RSM 的 Spring Data JDBC 存储实现（可选）
```

### lingyun-base-core

**依赖**: 仅 Lombok（optional, compile-time）

```
com.lingyun.base/
├── user/
│   └── IdentifiedUser.java          Serializable getId() — 身份抽象接口
└── request/
    └── CustomRequestAttributes<T>.java  泛型请求属性存储抽象基类
```

### lingyun-base-rsm

**依赖**: `lingyun-base-core` + Spring Web（不含 Web MVC）+ Spring AOP + Hibernate Validator + Hutool + Jackson  
**ORM 解耦**: RSM 不依赖任何 ORM；`ResponseMessage` 为纯 POJO，表映射由子模块（如 rsm-mybatis）负责  
**Web MVC 解耦**: RSM 仅依赖 `spring-web`，`ResponseBodyAdvice` 适配在 `rsm-mvc` 模块

```
com.lingyun.base.rsm/
├── RsmAutoConfiguration.java        Spring Boot 自动配置入口
├── R.java                           静态工具类：error() + msg() + params() + h_msg() + h_params()
├── ResponseBuilder<T>.java          响应构造器抽象 — 泛型 T 允许项目自定义响应结构
├── RsmManager.java                  消息声明基类 — 子类用 @RsmInfo 注解声明消息
├── RsmLoader.java                   启动时扫描 RsmManager 并同步消息到数据库
├── JsonResponseBodyPacker.java      核心 JSON 响应包装器
├── MessageResponseBuilder.java      默认 ResponseBuilder<Response> 实现
├── annotation/
│   ├── EnableRsm.java               @EnableRsm — 显式启用 RSM 自动配置
│   ├── RsmInfo.java                 @RsmInfo(template, status) — 声明消息
│   ├── ExecutionSuccess.java        @ExecutionSuccess(value) — 成功时消息键
│   ├── ExecutionFailed.java         @ExecutionFailed(value) — 失败时消息键
│   ├── BodyPackSetting.java         @BodyPackSetting — 包装行为控制
│   └── NotPack.java                 @NotPack — 标记方法/类退出响应自动包装
├── ResponsePackagingActuator.java   响应包装执行器接口（责任链模式）
├── ResponsePackagingActuatorManager.java  执行器链管理
├── AnnotationPackagingActuator.java  注解驱动包装（@ExecutionSuccess/Failed）
├── DefaultResponsePackagingActuator.java  兜底执行器
├── ErrorPackagingActuator.java      错误包装执行器标记接口
├── RsmRequestAttribute.java         请求级属性存储
├── AnnotationResponsePackConfiguration.java  配置属性（http.response.packer.annotation.*）
├── GenericRsm.java                  通用 CRUD 消息定义
├── HttpStatusRsm.java               HTTP 状态码 → 中文消息映射
├── exception/
│   └── RequestException.java        业务请求异常 — msgId + varargs
├── message/
│   ├── Response.java                默认响应体 { code, data, msg, type }
│   ├── ResponseType.java            SUCCESS / WARN / INFO / ERROR
│   ├── MessageWithParams.java       消息键 + 参数载体
│   ├── ResponseMessage.java         响应消息实体（纯 POJO，无 ORM 耦合）
│   └── ResponseMessageService.java  消息存储抽象接口
└── validation/
    ├── BaseValidationRsm.java       Jakarta/Hibernate 标准约束的中文消息模板
    ├── DatabaseMessageInterpolator.java  数据库驱动的验证消息插值器
    ├── SimpleMessageInterpolatorContext.java  验证上下文包装
    ├── FormValidationErrorMessages.java  表单验证错误集合
    ├── Validation2UnifyMessageErrorAdapter.java  统一处理 3 种验证异常
    └── ValidationConfiguration.java  验证插值器条件装配
```

### lingyun-base-rsm-mvc

**依赖**: `lingyun-base-rsm` + Spring Web MVC（spring-boot-starter-web）  
**按需引入**: 引入即自动注册 `ResponseBodyAdvice` 适配器和 ErrorController 包装

```
com.lingyun.base.rsm.mvc/
├── MvcRsmAutoConfiguration.java       Spring Boot MVC 适配自动配置
├── EnableRsm4Mvc.java                 @EnableRsm4Mvc — 显式启用 MVC 全自动配置
├── JsonResponseBodyPackerMvcAdapter.java  @ControllerAdvice + ResponseBodyAdvice
├── MvcErrorPackagingActuator.java     ErrorController 识别执行器
└── UnifiedFailureResponse.java        @Aspect 全局异常捕获 → RSM 错误响应
```

### lingyun-base-rsm-mybatis

**依赖**: `lingyun-base-rsm` + MyBatis-Plus

```
com.lingyun.base.rsm.mybatis/
├── MpResponseMessage.java                    extends ResponseMessage + @TableName + @TableId
├── ResponseMessageMapper.java                MyBatis-Plus BaseMapper<MpResponseMessage>
└── MybatisResponseMessageService.java        实现 ResponseMessageService（组合 Mapper）
```

### lingyun-base-rsm-jdbc

**依赖**: `lingyun-base-rsm` + Spring Data JDBC  
**设计**: 与 MyBatis-Plus 实现平行——验证 `ResponseMessageService` 接口的 ORM 无关性

```
com.lingyun.base.rsm.jdbc/
├── JdbcResponseMessage.java          extends ResponseMessage + @Table + @Id + Persistable
├── ResponseMessageRepository.java    CrudRepository<JdbcResponseMessage, String>
└── JdbcResponseMessageService.java  实现 ResponseMessageService（组合 Repository）
```

## Dependency Architecture

```
lingyun-base-rsm-mybatis ──→ lingyun-base-rsm ──→ lingyun-base-core
lingyun-base-rsm-jdbc    ──→ lingyun-base-rsm          (纯JDK)
lingyun-base-rsm-mvc     ──→ lingyun-base-rsm

lingyun-authorization-security ──→ lingyun-authorization-core ──→ lingyun-base-core
                                    (零框架契约)
lingyun-authorization-security ──→ lingyun-base-rsm
```

## Design Decisions

1. **RSM 与 Web MVC 分层解耦**  
   `lingyun-base-rsm` 仅依赖 `spring-web`（不含 `spring-webmvc`）。MVC 适配在 `rsm-mvc` 模块，引入即可自动对接 `ResponseBodyAdvice`。

2. **注解不含 `@ResponseBody`**  
   `@ExecutionSuccess` / `@ExecutionFailed` / `@BodyPackSetting` 不含 `@ResponseBody`，`@RestController` 已提供该语义。

3. **`ResponseBuilder<T>` 的泛型 `T` 是扩展点**  
   不同项目可传入自己的响应结构。`Response` 是建议实现，非强制约束。

4. **`ResponseMessage` 为纯 POJO，ORM 注解隔离在子类**  
   `MpResponseMessage` 和 `JdbcResponseMessage` 均 extends `ResponseMessage`，各自添加 ORM 注解。**messageKey 为天然主键**（加 `@Id`），code 为流水号。

5. **`ResponseMessageService` 为纯接口**  
   不继承任何 ORM 特定接口。实现类用**组合而非继承**。

6. **条件装配保证优雅降级**  
   `RsmLoader` / `MessageResponseBuilder` / `ValidationConfiguration` 使用 `@ConditionalOnBean`，无 `ResponseMessageService` 时 RSM 仍可用。

7. **`Role` 与 Spring Security 解耦**  
   `getAuthorities()` 返回 `Collection<String>`，转换工作交由 `CertifiedUser`。

8. **Dev/Prod 双模式**  
   通过 `lingyun.auth.filter.token-parse` 和 `lingyun.auth.custom.manager` 控制（值：`prod` / `dev`）。

## lingyun-authorization Module Structure

```
lingyun-authorization/                      ← 聚合 POM (groupId=com.lingyun)
├── lingyun-authorization-core/             ← 零框架依赖的认证授权契约
└── lingyun-authorization-security/         ← Spring Security 集成实现
```

### lingyun-authorization-core

**依赖**: 仅 `lingyun-base-core`（零框架依赖）

```
com.lingyun.authorization.core/
├── api/
│   ├── ResourceInfo.java              API 资源信息接口 — id, isOpen, getName
│   ├── ResourceInfoService<T>         路径→资源匹配服务 — matchPath, optMatchPath
│   └── annotation/
│       └── IsOpen.java                @IsOpen — 标记端点无需认证
├── entity/
│   ├── User.java                      interface — getId, getEnable, getLocked
│   ├── Role.java                      interface — getName, getAuthorities()→Collection<String>, getRouteIds()
│   └── Credential.java                interface — 凭证标记
└── session/
    ├── SessionManager.java             interface — parse, issue, remove, logout
    ├── UserToken.java                  record(id, serial)
    ├── CertificationChecker<T>         认证检查器接口
    └── CertificationService.java       认证服务接口
```

### lingyun-authorization-security

**依赖**: `lingyun-authorization-core` + `lingyun-base-rsm` + `spring-boot-starter-security` + `spring-boot-starter-web`

```
com.lingyun.authorization.security/
├── SecurityAutoConfiguration.java        Spring Boot 安全自动配置入口
├── SecurityFilterChainHelper.java        SecurityFilterChain 构建辅助器
├── EnableLingYunSecurity.java            @EnableLingYunSecurity — 显式启用安全配置
├── LingYunSecurityProperties.java        @ConfigurationProperties("lingyun.auth")
├── CertifiedUser.java                    Spring Security Authentication + IdentifiedUser 实现
├── CustomAuthorizationManager.java       鉴权管理器接口
├── ProdAuthorizationManager.java         生产环境鉴权（5 级优先级链）
├── DevAuthorizationManager.java          开发环境鉴权（始终放行）
├── CustomAccessDeniedHandler.java        403 拒绝访问 → RSM 统一响应
├── AuthorizationRequestAttribute<T>      请求属性存储（泛型）
├── AuthenticationRsm.java                RsmManager — 认证消息键定义
├── AnnotationResourceInfo.java           ResourceInfo POJO 实现
├── ResourceAuthorityMappingManager<T>    权限映射管理器接口
├── ServletMvcResourceAuthorityMappingManager  基于 RequestMappingHandlerMapping 的映射实现
├── AutoResourceInfoServiceImpl.java      默认 ResourceInfoService（AntPathMatcher）
├── ResourceInfoAutoConfiguration.java    资源信息自动配置
├── filter/
│   ├── TokenParseFilter.java             抽象 Token 解析过滤器（Ordered, +20）
│   ├── ResourceFilter.java               资源信息解析过滤器（Ordered, +10）
│   ├── ProdTokenParseFilter.java         生产环境 Token 解析（Bearer Token）
│   └── DevTokenParseFilter.java          开发环境 Token 解析（默认用户 id=2）
```

## 鉴权优先级（ProdAuthorizationManager）

1. errorPath 路径 → 放行
2. 无 ResourceInfo（未配置资源映射）→ 已认证即放行
3. ResourceInfo.isOpen() → 放行（`@IsOpen` 公开端点）
4. CertifiedUser 拥有 resource.id() 对应权限 → 放行
5. 否则 → 403 拒绝

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
      token-parse: prod    # prod | dev
    custom:
      manager: prod        # prod | dev
```

## 后续规划

- [ ] `lingyun-base-cache` — EnhancedRedisCacheManager + TTL 语法糖
- [ ] `lingyun-base-mail` — EmailService 接口 + 实现（from 可配置）
- [ ] `lingyun-base-query` — AbstractQuery + Equals/Like/Range/Sort 查询 DSL
- [ ] `lingyun-base-mybatis` — MetaObjectHandler 自动填充
- [ ] `lingyun-base-validation` — 拆出独立的验证集成模块
