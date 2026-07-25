# LingYun Base RSM

> 响应标准化框架 —— Response Standardization Middleware

## 定位

RSM 是 Ling-Yun 的核心响应框架，提供统一的 API 响应体格式、声明式消息管理、注解驱动的包装控制。**与 ORM 解耦、与 Web MVC 解耦**——消息存储由子模块（mybatis / jdbc）实现，MVC 适配由 `rsm-mvc` 可选引入。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 快速开始

### 1. 启用 RSM

在任意 `@Configuration` 类上标注 `@EnableRsm`：

```java
@Configuration
@EnableRsm
public class AppConfig {}
```

或通过 Spring Boot 自动配置——只要 classpath 中存在 `lingyun-base-rsm` 即自动生效。

### 2. 定义消息

继承 `RsmManager`，用 `@RsmInfo` 声明消息：

```java
@Component
public class MyRsm extends RsmManager {
    @RsmInfo(template = "用户 {0} 创建成功", status = HttpStatus.CREATED)
    public static final String USER_CREATED = "My_user_created";
}
```

应用启动时 `RsmLoader` 自动将消息同步到数据库。

### 3. 使用 R 工具类

```java
@RestController
public class UserController {
    @GetMapping("/users")
    public Response getUsers() {
        return R.msg(MyRsm.USER_CREATED, "张三").data(userList);
    }

    @PostMapping("/users")
    public Response createUser(@Valid @RequestBody UserDto dto) {
        // 正常返回自动包装
        return R.msg(MyRsm.USER_CREATED, dto.getName()).data(userService.save(dto));
    }
}
```

### 4. 注解驱动包装

```java
@RestController
public class OrderController {

    @ExecutionSuccess("Order_created")
    @ExecutionFailed("Order_create_failed")
    @PostMapping("/orders")
    public Order createOrder(@RequestBody OrderDto dto) {
        return orderService.create(dto);
    }
}
```

## 核心组件

| 类 | 说明 |
|---|---|
| `R` | 静态工具类，提供 `error()`、`msg()`、`params()` 等方法 |
| `ResponseBuilder<T>` | 响应构造器抽象——不同项目可传入自定义响应结构 |
| `RsmManager` | 消息声明基类，用 `@RsmInfo` 注解字段声明消息 |
| `RsmLoader` | 启动时扫描所有 `RsmManager`，同步消息到数据库 |
| `JsonResponseBodyPacker` | 核心 JSON 响应包装逻辑 |
| `ResponsePackagingActuator` | 响应包装执行器接口（责任链模式） |
| `ResponsePackagingActuatorManager` | 执行器链管理器 |
| `AnnotationPackagingActuator` | 注解驱动包装（识别 `@ExecutionSuccess`/`@ExecutionFailed`） |
| `DefaultResponsePackagingActuator` | 兜底执行器 |
| `MessageResponseBuilder` | 默认响应构建器（数据库消息模板） |
| `RsmRequestAttribute` | 请求级属性存储（MESSAGE / HEADER_MESSAGE 等） |

## 验证集成

| 类 | 说明 |
|---|---|
| `BaseValidationRsm` | Jakarta/Hibernate 标准约束的中文消息模板 |
| `DatabaseMessageInterpolator` | 数据库驱动的验证消息插值器 |
| `Validation2UnifyMessageErrorAdapter` | 统一处理 3 种验证异常为 RSM 格式 |
| `ValidationConfiguration` | 条件装配验证插值器 |

## 注解一览

| 注解 | 说明 |
|---|---|
| `@EnableRsm` | 启用 RSM 自动配置（显式声明） |
| `@RsmInfo` | 声明消息键的模板和 HTTP 状态码 |
| `@ExecutionSuccess` | 方法成功时使用的消息键 |
| `@ExecutionFailed` | 方法失败时使用的消息键 |
| `@BodyPackSetting` | 响应包装行为控制（enable/success/failed） |
| `@NotPack` | 标记方法/类退出自动包装 |

## 消息存储扩展

RSM 本身**不依赖任何 ORM**。`ResponseMessage` 为纯 POJO，表映射由子模块负责：

- **MyBatis-Plus** → `lingyun-base-rsm-mybatis`
- **Spring Data JDBC** → `lingyun-base-rsm-jdbc`

二选一引入即可。

## Spring Web MVC 适配

RSM 核心仅依赖 `spring-web`（不含 `spring-webmvc`），如需 `ResponseBodyAdvice` 自动包装，额外引入：

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm-mvc</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 配置属性

```yaml
http:
  response:
    packer:
      annotation:
        auto: true
        default-success-message: "Generic_execution_success"
        default-failed-message: "Generic_execution_failed"
```
