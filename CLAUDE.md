# CLAUDE.md

This file provides guidance to Claude Code when working in this repository.

## Project Overview

**LingYun** — 可独立复用的 Java 基础设施库。所有 starter 模块引入即自动配置。

- **语言**: Java 17
- **构建**: Maven 多模块
- **版本**: 1.0.1

## 顶层模块

```
Ling-Yun/
├── pom.xml                                         ← 根 POM（版本管理）
├── lingyun-base/                                   ← 基础设施库
│   ├── lingyun-base-core/                          ← 纯 JDK 核心
│   └── lingyun-base-rsm/                           ← 响应标准化框架
├── lingyun-authorization/                          ← 认证授权框架
│   ├── lingyun-authorization-core/                 ← 核心契约（零框架依赖）
│   └── lingyun-authorization-security/             ← Spring Security 集成（artifactId 含 -starter 后缀）
├── rsm-mvc-spring-boot-starter/                    ← RSM 的 MVC 适配
├── rsm-mybatisplus-spring-boot-starter/            ← RSM 的 MyBatis-Plus 存储
└── rsm-jdbc-spring-boot-starter/                   ← RSM 的 JDBC 存储
```

## 快捷注解（可选——自动配置已覆盖）

| 注解 | 说明 |
|---|---|
| `@EnableRsm` | 显式启用 RSM 核心自动配置 |
| `@EnableRsm4Mvc` | 显式启用 RSM + Spring MVC 全自动配置 |
| `@EnableLingYunSecurity` | 显式启用认证授权 + Spring Security 集成 |

> 所有 `-spring-boot-starter` 模块通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 自动加载，无需注解。

## lingyun-base Module Structure

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

**依赖**: `lingyun-base-core` + Spring Web（不含 MVC）+ Jackson + Hibernate Validator + Hutool

```
com.lingyun.base.rsm/
├── RsmAutoConfiguration.java          Spring Boot 自动配置入口
├── R.java                             静态工具类：error() + msg() + params() + h_msg() + h_params()
├── ResponseBuilder<T>.java            响应构造器抽象
├── RsmManager.java                    消息声明基类 — @RsmInfo 声明消息
├── RsmLoader.java                     启动时扫描并同步消息
├── JsonResponseBodyPacker.java        核心 JSON 响应包装器
├── MessageResponseBuilder.java        默认 ResponseBuilder<Response> 实现
├── annotation/
│   ├── EnableRsm.java                 @EnableRsm
│   ├── RsmInfo.java                   @RsmInfo(template, status)
│   ├── ExecutionSuccess.java          @ExecutionSuccess(value)
│   ├── ExecutionFailed.java           @ExecutionFailed(value)
│   ├── BodyPackSetting.java           @BodyPackSetting
│   └── NotPack.java                   @NotPack
├── ResponsePackagingActuator.java      响应包装执行器接口（责任链）
├── ResponsePackagingActuatorManager.java  执行器链管理
├── AnnotationPackagingActuator.java    注解驱动包装
├── DefaultResponsePackagingActuator.java  兜底执行器
├── ErrorPackagingActuator.java         错误包装执行器标记接口
├── RsmRequestAttribute.java            请求级属性存储
├── AnnotationResponsePackConfiguration.java  配置属性
├── GenericRsm.java                     通用 CRUD 消息定义
├── HttpStatusRsm.java                  HTTP 状态码 → 中文消息映射
├── str/
│   └── RString.java                    String 包装类型（解决 Controller 返回 String 无法自动包装的问题）
├── exception/
│   └── RequestException.java           业务请求异常 — msgId + varargs
├── message/
│   ├── Response.java                   默认响应体 { code, data, msg, type }
│   ├── ResponseType.java              SUCCESS / WARN / INFO / ERROR
│   ├── MessageWithParams.java          消息键 + 参数载体
│   ├── ResponseMessage.java            响应消息实体（纯 POJO）
│   ├── ResponseMessageService.java     消息存储抽象接口
│   └── DefaultResponseMessageServiceImpl.java  内存默认实现（无数据库时兜底）
└── validation/
    ├── BaseValidationRsm.java          标准约束中文消息模板
    ├── DatabaseMessageInterpolator.java  数据库驱动的验证消息插值器
    ├── SimpleMessageInterpolatorContext.java  验证上下文包装
    ├── FormValidationErrorMessages.java  表单验证错误集合
    ├── Validation2UnifyMessageErrorAdapter.java  统一处理 3 种验证异常
    └── ValidationConfiguration.java    验证插值器条件装配
```

### rsm-mvc-spring-boot-starter

**路径**: 项目根目录（独立 starter）

```
com.lingyun.base.rsm.mvc/
├── MvcRsmAutoConfiguration.java        自动配置（含 @EnableRsm）
├── EnableRsm4Mvc.java                  可选显式声明注解
├── JsonResponseBodyPackerMvcAdapter.java  @ControllerAdvice + ResponseBodyAdvice
├── MvcErrorPackagingActuator.java     ErrorController 识别执行器
└── UnifiedFailureResponse.java        @Aspect 全局异常捕获 → RSM 错误响应
```

### rsm-mybatisplus-spring-boot-starter

**路径**: 项目根目录（独立 starter）

```
com.lingyun.base.rsm.mybatis/
├── RsmMybatisPlusPluginAutoConfiguration.java  自动配置（含 @EnableRsm）
├── MpResponseMessage.java                 extends ResponseMessage + @TableName + @TableId
├── ResponseMessageMapper.java              MyBatis-Plus BaseMapper
└── MybatisResponseMessageService.java      实现 ResponseMessageService（组合 Mapper）
```

### rsm-jdbc-spring-boot-starter

**路径**: 项目根目录（独立 starter）

```
com.lingyun.base.rsm.jdbc/
├── RsmJdbcPluginAutoConfiguration.java   自动配置（含 @EnableRsm）
├── JdbcResponseMessage.java              extends ResponseMessage + @Table + @Id + Persistable
├── ResponseMessageRepository.java        CrudRepository
└── JdbcResponseMessageService.java       实现 ResponseMessageService（组合 Repository）
```

## Dependency Architecture

```
rsm-mybatisplus-spring-boot-starter ──→ lingyun-base-rsm ──→ lingyun-base-core
rsm-jdbc-spring-boot-starter       ──→ lingyun-base-rsm
rsm-mvc-spring-boot-starter        ──→ lingyun-base-rsm

lingyun-authorization-security-spring-boot-starter
  ──→ lingyun-authorization-core ──→ lingyun-base-core
  ──→ lingyun-base-rsm
```

## Design Decisions

1. **RSM 与 Web MVC 分层解耦** — RSM 仅依赖 `spring-web`，MVC 适配为独立 starter
2. **注解不含 `@ResponseBody`** — `@RestController` 已提供该语义
3. **`ResponseBuilder<T>` 泛型是扩展点** — 不同项目可传入自定义响应结构
4. **`ResponseMessage` 纯 POJO** — ORM 注解隔离在子类，`messageKey` 为天然主键
5. **组合优于继承** — 各 Service 实现组合持久化接口，不继承基类
6. **内存兜底** — 无数据库时 `DefaultResponseMessageServiceImpl` / `InMemorySessionManager` 自动启用
7. **Spring Boot auto-configuration.imports** — 所有 starter 自动加载，`@Enable*` 注解可选
8. **Dev/Prod 双模式** — 通过 `lingyun.auth.filter.token-parse` 和 `lingyun.auth.custom.manager` 控制

## lingyun-authorization Module Structure

### lingyun-authorization-core

**依赖**: 仅 `lingyun-base-core`

```
com.lingyun.authorization.core/
├── api/
│   ├── ResourceInfo.java              API 资源信息接口 — id, isOpen, getName
│   ├── ResourceInfoService<T>         路径→资源匹配服务
│   └── annotation/
│       └── IsOpen.java                @IsOpen — 标记端点无需认证
├── entity/
│   ├── User.java                      interface — getId, getEnable, getLocked
│   ├── Role.java                      interface — getName, getAuthorities()→Collection<String>, getRouteIds()
│   ├── Credential.java                interface — 凭证标记
│   └── CertifiedUser<T>               interface — 已认证用户抽象（不依赖任何安全框架）
├── session/
│   ├── SessionManager.java             interface — parse, issue, remove
│   ├── InMemorySessionManager.java     SessionManager 内存默认实现（UUID Token）
│   ├── UserToken.java                  record(id, serial)
│   ├── CertificationChecker<T>         认证检查器接口
│   └── CertificationService.java       认证服务接口
└── spi/
    └── RoleProvider.java               SPI 扩展点 — 消费方提供角色数据
```

### lingyun-authorization-security-spring-boot-starter

**路径**: `lingyun-authorization/lingyun-authorization-security/`（目录名不含 starter 后缀，artifactId 含）

```
com.lingyun.authorization.security/
├── SecurityAutoConfiguration.java          自动配置入口（条件装配所有默认实现）
├── SecurityFilterChainHelper.java          SecurityFilterChain 构建辅助器
├── EnableLingYunSecurity.java              可选显式声明注解
├── LingYunSecurityProperties.java          @ConfigurationProperties("lingyun.auth")
├── SecurityCertificationChecker.java       CertificationChecker 的 Spring Security 默认实现
├── SecurityCertifiedUserImpl.java          Authentication + CertifiedUser 实现
├── CustomAuthorizationManager.java         鉴权管理器接口
├── ProdAuthorizationManager.java           生产环境鉴权（5 级优先级链）
├── DevAuthorizationManager.java            开发环境鉴权（始终放行）
├── CustomAccessDeniedHandler.java          403 → RSM 统一响应
├── AuthorizationRequestAttribute<T>        请求属性存储（泛型）
├── AuthenticationRsm.java                  RsmManager — 认证消息键定义
├── AnnotationResourceInfo.java             ResourceInfo POJO 实现
├── ResourceAuthorityMappingManager<T>      权限映射管理器接口
├── ServletMvcResourceAuthorityMappingManager  基于 RequestMappingHandlerMapping 的映射实现
├── AutoResourceInfoServiceImpl.java        默认 ResourceInfoService（AntPathMatcher）
├── ResourceInfoAutoConfiguration.java      资源信息自动配置
├── filter/
│   ├── TokenParseFilter.java              抽象 Token 解析过滤器（Ordered, +20）
│   ├── ResourceFilter.java                资源信息解析过滤器（Ordered, +10）
│   ├── ProdTokenParseFilter.java          生产环境 Token 解析（Bearer Token）
│   └── DevTokenParseFilter.java           开发环境 Token 解析（默认用户）
```

## 鉴权优先级（ProdAuthorizationManager）

1. errorPath 路径 → 放行
2. 无 ResourceInfo → 已认证即放行
3. ResourceInfo.isOpen() → 放行（`@IsOpen`）
4. CertifiedUser 拥有 resource.id() 对应权限 → 放行
5. 否则 → 403

## 消费方扩展点

| 接口 | 所在模块 | 默认实现 | 说明 |
|---|---|---|---|
| `ResponseMessageService` | base-rsm | `DefaultResponseMessageServiceImpl` | 消息存储，替换为 `rsm-mybatisplus/jdbc-starter` |
| `ResponseBuilder<T>` | base-rsm | `MessageResponseBuilder` | 响应体结构 |
| `SessionManager` | auth-core | `InMemorySessionManager` | Token 签发/解析 |
| `CertificationChecker<T>` | auth-core | `SecurityCertificationChecker` | 用户认证检查 |
| `RoleProvider` | auth-core (spi) | 无 | 角色数据提供（消费方必须实现） |
| `ResourceInfoService<T>` | auth-core | `AutoResourceInfoServiceImpl` | API 资源匹配 |

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
- [ ] `lingyun-base-mail` — EmailService 接口 + 实现
- [ ] `lingyun-base-query` — AbstractQuery + Equals/Like/Range/Sort 查询 DSL
- [ ] `lingyun-base-mybatis` — MetaObjectHandler 自动填充
