# LingYun Base RSM

> 响应标准化框架（Response Standardization Middleware）

## 定位

提供统一的 API 响应格式、声明式消息管理、注解驱动包装控制。与 ORM 解耦、与 MVC 解耦——消息存储由 Starter 模块实现，MVC 适配由 `rsm-mvc-spring-boot-starter` 可选引入。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 核心组件

| 类 | 说明 |
|---|---|
| `R` | 静态工具类：`error()` / `msg()` / `params()` |
| `ResponseBuilder<T>` | 响应构造器抽象——泛型 T 可扩展 |
| `RsmManager` | 消息声明基类，`@RsmInfo` 注解字段声明消息 |
| `RsmLoader` | 启动时扫描并同步消息到存储 |
| `DefaultResponseMessageServiceImpl` | 默认内存实现，无数据库时自动启用 |
| `JsonResponseBodyPacker` | 核心 JSON 响应包装逻辑 |
| `ResponsePackagingActuatorManager` | 执行器链管理器 |
| `AnnotationPackagingActuator` | `@ExecutionSuccess`/`@ExecutionFailed` 驱动 |
| `DefaultResponsePackagingActuator` | 兜底执行器 |

## 注解

| 注解 | 说明 |
|---|---|
| `@EnableRsm` | 显式启用 RSM 自动配置 |
| `@RsmInfo` | 声明消息键的模板和 HTTP 状态码 |
| `@ExecutionSuccess` | 方法成功时的消息键 |
| `@ExecutionFailed` | 方法失败时的消息键 |
| `@BodyPackSetting` | 包装行为控制 |
| `@NotPack` | 退出自动包装 |

## 验证集成

`DatabaseMessageInterpolator` — 数据库驱动的验证消息插值器；`Validation2UnifyMessageErrorAdapter` — 统一处理 3 种验证异常。

## 消息存储扩展

RSM 无数据库时自动启用内存存储。引入 Starter 切换为 DB 实现：

- **MyBatis-Plus** → `rsm-mybatisplus-spring-boot-starter`
- **Spring Data JDBC** → `rsm-jdbc-spring-boot-starter`

## MVC 适配

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>rsm-mvc-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

引入即自动注册 `ResponseBodyAdvice` + 全局异常包装。

## 配置

```yaml
http:
  response:
    packer:
      annotation:
        auto: true
        default-success-message: "Generic_execution_success"
        default-failed-message: "Generic_execution_failed"
```
