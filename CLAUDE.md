# CLAUDE.md

This file provides guidance to Claude Code when working in this repository.

## Project Overview

**LingYun** — 一组可从 YuNi 项目中独立复用的 Java 基础设施库。设计原则：**core 定义契约（零框架依赖），上层模块提供框架适配实现**。

- **语言**: Java 17
- **构建**: Maven 多模块
- **来源**: 从 YuNi（梓渝小站）Spring Boot 3.4.5 项目中剥离

## 顶层模块

```
Ling-Yun/
├── pom.xml                            ← 所有模块的公共父 POM（版本管理）
├── lingyun-base/                      ← 基础设施库（缓存、邮件、查询、RSM 等）
└── lingyun-authorization/             ← 认证授权框架（core 契约 + Spring Security 集成）
```

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
├── annotation/
│   └── IsOpen.java                 @IsOpen — 标记 API 无需认证（已迁至 lingyun-authorization-core）
└── user/
    └── IdentifiedUser.java          Serializable getId() — 身份抽象接口
```

### lingyun-base-rsm

**依赖**: `lingyun-base-core` + Spring Web（不含 Web MVC）+ Spring AOP + Hibernate Validator + Hutool + Jackson  
**ORM 解耦**: RSM 不依赖任何 ORM；`ResponseMessage` 为纯 POJO，表映射由子模块（如 rsm-mybatis）负责  
**Web MVC 解耦**: `JsonResponseBodyPacker` 不实现 `ResponseBodyAdvice`；`ErrorPackagingActuator` 已迁至 `lingyun-base-rsm-mvc`。引入 MVC 适配模块即可自动接入

```
com.lingyun.base.rsm/
├── R.java                          error() + msg() + params() + h_msg() + h_params()
├── ResponseBuilder<T>.java         响应构造器抽象 — 泛型 T 允许项目自定义响应结构
├── RsmManager.java                 消息声明接口 — 子类用 @RsmInfo 注解声明消息
├── RsmLoader.java                  启动时扫描 RsmManager 并同步消息到存储层（需要 ResponseMessageService bean）
├── ResponsePackagingActuator.java  响应包装执行器接口
├── ResponsePackagingActuatorManager.java  执行器链管理
├── AnnotationPackagingActuator.java  注解驱动包装（识别 @ExecutionSuccess/Failed）
├── DefaultResponsePackagingActuator.java  兜底执行器
├── JsonResponseBodyPacker.java     @Component — 核心包装逻辑（MVC 适配器见 rsm-mvc 模块）
├── UnifiedFailureResponse.java     @Aspect 捕获未处理异常 → RequestException
├── MessageResponseBuilder.java     默认 ResponseBuilder<Response> 实现（需 ResponseMessageService bean, @ConditionalOnBean）
├── RsmRequestAttribute.java        请求级属性存储（MESSAGE/HEADER_MESSAGE/CONFIRMED_RESPONSE_MESSAGE）
├── AnnotationResponsePackConfiguration.java  配置属性（http.response.packer.annotation.*）
├── GenericRsm.java                 通用 CRUD 消息定义（QUERY_SUCCESS, CREATE_SUCCESS 等）
├── HttpStatusRsm.java              HTTP 状态码 → 中文消息映射
├── exception/
│   └── RequestException.java       业务请求异常 — msgId + varargs
├── annotation/
│   ├── RsmInfo.java                @RsmInfo(template, status) — 声明消息
│   ├── ExecutionSuccess.java       @ExecutionSuccess(value) — 成功时消息键
│   ├── ExecutionFailed.java        @ExecutionFailed(value) — 失败时消息键
│   ├── BodyPackSetting.java        @BodyPackSetting — 包装行为控制
│   └── NotPack.java                @NotPack — 标记方法/类退出响应自动包装
├── message/
│   ├── Response.java               默认响应体 { code, data, msg, type }
│   ├── ResponseType.java           SUCCESS / WARN / INFO / ERROR
│   ├── MessageWithParams.java      消息键 + 参数载体（R.msg() 使用）
│   ├── ResponseMessage.java        响应消息实体（纯 POJO，无 ORM 耦合）
│   └── ResponseMessageService.java 消息存储抽象接口（findByMessageKey / list / save / updateById）
└── validation/
    ├── BaseValidationRsm.java      Jakarta/Hibernate 标准约束的中文消息模板
    ├── DatabaseMessageInterpolator.java  数据库驱动的验证消息插值器
    ├── SimpleMessageInterpolatorContext.java  验证上下文包装
    ├── FormValidationErrorMessages.java  表单验证错误集合
    ├── Validation2UnifyMessageErrorAdapter.java  @RestControllerAdvice 统一处理 3 种验证异常
    └── ValidationConfiguration.java  @ConditionalOnBean(ResponseMessageService) 注册验证插值器
```

### lingyun-base-rsm-mvc

**依赖**: `lingyun-base-rsm` + Spring Web MVC（spring-boot-starter-web）  
**按需引入**: 仅当项目使用 Spring Web MVC 时才需引入——自动注册 `ResponseBodyAdvice` 适配器和 ErrorController 包装

```
com.lingyun.base.rsm.mvc/
├── JsonResponseBodyPackerMvcAdapter.java  @ControllerAdvice + ResponseBodyAdvice — 委托给 JsonResponseBodyPacker
└── ErrorPackagingActuator.java            ResponsePackagingActuator 实现 — 识别 ErrorController
```

### lingyun-base-rsm-mybatis

**依赖**: `lingyun-base-rsm` + MyBatis-Plus

```
com.lingyun.base.rsm.mybatis/
├── MpResponseMessage.java                    extends ResponseMessage + @TableName + @TableId（ORM 注解隔离）
├── ResponseMessageMapper.java                MyBatis-Plus BaseMapper<MpResponseMessage>
└── MybatisResponseMessageService.java        实现 ResponseMessageService（组合 Mapper）
```

### lingyun-base-rsm-jdbc

**依赖**: `lingyun-base-rsm` + Spring Data JDBC（spring-boot-starter-data-jdbc）  
**设计**: 与 MyBatis-Plus 实现平行的另一种存储方案，用于验证 `ResponseMessageService` 接口的 ORM 无关性

```
com.lingyun.base.rsm.jdbc/
├── JdbcResponseMessage.java          extends ResponseMessage + @Table + @Id + Persistable（ORM 注解隔离）
├── ResponseMessageRepository.java    CrudRepository<JdbcResponseMessage, Integer>
└── JdbcResponseMessageService.java  实现 ResponseMessageService（组合 Repository）
```

## Dependency Architecture

```
lingyun-base-rsm-mybatis ──→ lingyun-base-rsm ──→ lingyun-base-core
 (MyBatis-Plus 存储实现)      (响应框架 + 接口)      (纯 JDK 核心)

lingyun-base-rsm-jdbc ──→ lingyun-base-rsm
 (Spring Data JDBC 存储)     (响应框架 + 接口)

lingyun-base-rsm-mvc ──→ lingyun-base-rsm
 (MVC 适配层)               (响应框架 + 接口)
```

**按需引入**：
- 只要 RSM 框架：引入 `lingyun-base-rsm`（消息功能自动降级，响应体直通）
- RSM + Spring Web MVC：额外引入 `lingyun-base-rsm-mvc`（自动注册 `ResponseBodyAdvice` 适配器）
- RSM + MyBatis-Plus 消息存储：额外引入 `lingyun-base-rsm-mybatis`
- RSM + Spring Data JDBC 消息存储：额外引入 `lingyun-base-rsm-jdbc`
- 两种存储实现可互换——`RsmLoader` 只依赖 `ResponseMessageService` 接口，不关心实现

## Design Decisions

1. **RSM 与 Web MVC 分层解耦**  
   `lingyun-base-rsm` 仅依赖 `spring-web`（不含 `spring-webmvc`）。`JsonResponseBodyPacker` 退化为 `@Component` + 核心方法；`ErrorPackagingActuator` 迁至 `lingyun-base-rsm-mvc`。使用 MVC 的项目引入 `rsm-mvc` 模块即可自动对接 `ResponseBodyAdvice`。

2. **`@ExecutionSuccess` / `@ExecutionFailed` / `@BodyPackSetting` 不含 `@ResponseBody`**  
   去掉 `@ResponseBody` 注解是为了减少对 Spring Web 的依赖——`@RestController` 已提供 `@ResponseBody` 语义，注解上再加是冗余的。

3. **`ResponseBuilder<T>` 的泛型 `T` 是扩展点**  
   不同项目可以传入自己的响应结构。`Response` 是 RSM 自带的"建议实现"而非强制约束。

4. **`ResponseMessage` 为纯 POJO，ORM 注解放在各模块的实体子类中**  
   `MpResponseMessage`（MyBatis-Plus）和 `JdbcResponseMessage`（Spring Data JDBC）均 extends `ResponseMessage` 并添加各自 ORM 注解（`@TableName`/`@TableId` 或 `@Table`/`@Id`），**均在 messageKey（天然主键）而非 code（流水号）上加 @Id**。核心 POJO 与 ORM 完全解耦——这是验证扩展性的关键设计。

5. **`ResponseMessageService` 是纯接口，不继承 MyBatis-Plus `IService`**  
   RSM 不绑定任何 ORM。两个实现（`MybatisResponseMessageService`、`JdbcResponseMessageService`）均用**组合而非继承**——避免了泛型 diamond 继承问题。

6. **`RsmLoader` / `MessageResponseBuilder` / `ValidationConfiguration` 使用 `@ConditionalOnBean` / `@Autowired(required = false)`**  
   无 `ResponseMessageService` 实现时优雅降级，RSM 仍可启动但不提供消息模板解析。

7. **`R.java` 位于 rsm 模块**  
   唯一的 `R` 类提供 `error()` + `msg()` / `params()` / `h_msg()` / `h_params()`，依赖 Spring RequestAttributes。

8. **不耦合 YuNi**  
   `GenericRsm` 去掉了 `PAY_SUCCESS`/`PAY_FAILED` 等业务特定消息；`Gender` 枚举、`Api`/`ApiService` API 资源管理类均不纳入。

## 与 YuNi 原文的对照

| YuNi (com.daisy) | LingYun (com.lingyun) | 变化 |
|---|---|---|
| `yuni-base` | `lingyun-base-core` + `lingyun-base-rsm` + `lingyun-base-rsm-mvc` + `lingyun-base-rsm-mybatis` + `lingyun-base-rsm-jdbc` | 按职责拆成 5 个模块 |
| `RespMsgService extends IService` | `ResponseMessageService` (纯接口) | 去 MyBatis-Plus 耦合 |
| `RespMsgServiceImpl` | `MybatisResponseMessageService` + `JdbcResponseMessageService` | 两种 ORM 实现各一套，验证接口扩展性 |
| `ResponseTypeEnum` | `ResponseType` | 去掉冗余 Enum 后缀 |
| `@NotPack` 仅 METHOD | `@NotPack` 支持 TYPE + METHOD | 支持类级别退出包装 |
| `@ExecutionSuccess` 含 `@ResponseBody` + `contentType` | 纯注解，只保留 `value()` | 减少 Spring Web 依赖 |
| `GenericRsm` 含支付消息 | 移除 `PAY_SUCCESS`/`PAY_FAILED` | 去业务耦合 |
| `ResponseMessage` 直接加 `@TableName` | 纯 POJO → `MpResponseMessage` / `JdbcResponseMessage` 子类各自添加 ORM 注解 | ORM 注解与核心 POJO 完全解耦 |
| `JsonResponseBodyPacker` 直接实现 `ResponseBodyAdvice` | 拆为 `JsonResponseBodyPacker` (rsm) + `JsonResponseBodyPackerMvcAdapter` (rsm-mvc) | 去 spring-webmvc 依赖，MVC 适配层独立为可选模块 |
| `ErrorPackagingActuator` 在 rsm 模块 | 迁至 `lingyun-base-rsm-mvc` | ErrorController 属 MVC 范畴，独立模块 |
| `yuni-authorization` | `lingyun-authorization-core` + `lingyun-authorization-security` | 拆为契约层 + Spring Security 集成层；biz 层留在 YuNi |
| `Role.getAuthorities()` → `GrantedAuthority` | → `Collection<String>` | 核心契约与 Spring Security 解耦 |
| `@IsOpen` 在 `com.daisy.base` | 迁至 `lingyun-authorization-core.annotation` | 认证专用注解，不再放在通用 core 模块 |

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
├── annotation/
│   └── IsOpen.java                 @IsOpen — 标记端点无需认证（从 lingyun-base-core 迁入）
├── entity/
│   ├── User.java                   interface — getId, getEnable, getLocked
│   ├── Role.java                   interface — getName, getAuthorities()→Collection<String>（与 Spring Security 解耦）
│   └── Credential.java             interface — 凭证标记
└── session/
    ├── SessionManager.java          interface — parse, issue, remove, logout
    └── UserToken.java               record(id, serial)
```

### lingyun-authorization-security

**依赖**: `lingyun-authorization-core` + `lingyun-base-rsm` + `spring-boot-starter-security` + `spring-boot-starter-web`

```
com.lingyun.authorization.security/
├── CertifiedUser.java              Spring Security Authentication + IdentifiedUser 实现
├── SecurityConfig.java             SecurityFilterChain 配置（无状态会话）
├── CustomAuthorizationManager.java 鉴权管理器接口
├── ProdAuthorizationManager.java   生产环境鉴权（@ConditionalOnProperty app.env=prod）
├── DevAuthorizationManager.java    开发环境鉴权（始终放行）
├── ResourceInfo.java               资源信息接口（替代 YuNi 的 Api）
├── CustomAccessDeniedHandler.java  403 拒绝访问处理器
├── AuthorizationRequestAttribute.java  请求属性存储（泛型）
├── AuthenticationRsm.java          RsmManager — 认证相关消息键（17 个）
├── CertificationChecker.java       认证检查器接口 — authorize(User)→CertifiedUser
└── filter/
    ├── TokenParseFilter.java       抽象 Token 解析过滤器
    ├── ProdTokenParseFilter.java   生产环境 Token 解析
    └── DevTokenParseFilter.java    开发环境 Token 解析（默认用户）
```

## lingyun-authorization Dependency Architecture

```
lingyun-authorization-security ──→ lingyun-authorization-core ──→ lingyun-base-core
  (Spring Security + Web)          (零框架契约)                   (IdentifiedUser)

lingyun-authorization-security ──→ lingyun-base-rsm
  (RSM: R.error, RsmManager)
```

**按需引入**：
- 只要认证接口：引入 `lingyun-authorization-core`
- 认证 + Spring Security 集成：额外引入 `lingyun-authorization-security`（自动配置 SecurityFilterChain、Token 过滤器等）

## 设计决策：认证授权

1. **`@IsOpen` 归属于 authorization 模块**  
   从 `lingyun-base-core` 迁至 `lingyun-authorization-core`——它是认证授权领域的注解，不是通用基础设施。

2. **`Role.getAuthorities()` 返回 `Collection<String>` 而非 `GrantedAuthority`**  
   核心契约与 Spring Security 完全解耦。`CertifiedUser`（security 模块）负责将 plain string 转换为 `SimpleGrantedAuthority`。

3. **`IdentifiedUser` 位于 `lingyun-base-core`**  
   用户身份抽象（`Serializable getId()`）是通用概念，被 authorization 和其他模块共同使用。

4. **Dev/Prod 环境通过 `app.env` 切换**  
   `@ConditionalOnProperty(name = "app.env")` 自动激活对应的 Token 过滤器和鉴权管理器。

5. **不纳入 biz 层**  
   Controller、Service impl、MyBatis-Plus Entity、Email 认证器等业务相关代码留在 YuNi，仅提取框架层面的契约和 Spring Security 集成。

## 后续规划（TODO）

- [ ] `lingyun-base-cache` — EnhancedRedisCacheManager + TTL 语法糖（`#3600`）
- [ ] `lingyun-base-mail` — EmailService 接口 + 实现（from 可配置，去品牌名硬编码）
- [ ] `lingyun-base-query` — AbstractQuery + Equals/Like/Range/Sort 查询 DSL
- [ ] `lingyun-base-mybatis` — MetaObjectHandler 自动填充（去 YuNi 类名）
- [ ] `lingyun-base-validation` — 拆出独立的验证集成模块
- [x] `lingyun-authorization` 剥离 — 从 yuni-authorization 中提取可复用认证框架 ✓
