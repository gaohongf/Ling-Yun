# LingYun Base RSM MVC

> RSM 的 Spring Web MVC 适配层——按需引入

## 定位

`rsm-mvc` 是 RSM 框架的**可选扩展模块**，为使用 Spring Web MVC 的项目提供自动对接。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm-mvc</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 提供的功能

### 1. `ResponseBodyAdvice` 自动包装

`JsonResponseBodyPackerMvcAdapter` — 实现 Spring MVC 的 `ResponseBodyAdvice` 接口，自动拦截所有 `@ResponseBody` 返回值，委托给 `JsonResponseBodyPacker` 包装为 RSM 统一响应体。

引入即生效，无需额外配置。

### 2. ErrorController 异常包装

`MvcErrorPackagingActuator` — 识别 Spring Boot 的 `BasicErrorController` 路径，将错误响应包装为 RSM 格式。

### 3. 全局异常统一响应

`UnifiedFailureResponse` — `@Aspect` 切面，捕获 Controller 中未处理的异常，统一转换为 `RequestException`，再由 RSM 包装为错误响应体。

### 4. 显式启用

`@EnableRsm4Mvc` — 在 `@Configuration` 类上标注即可显式启用 MVC 适配（通常不需要，自动配置已覆盖）。

## 自动配置

`MvcRsmAutoConfiguration` — Spring Boot 自动配置，装配 `JsonResponseBodyPackerMvcAdapter`、`MvcErrorPackagingActuator`、`UnifiedFailureResponse`。只要 classpath 存在此模块即自动生效。

## 内容

| 类 | 说明 |
|---|---|
| `JsonResponseBodyPackerMvcAdapter` | `@ControllerAdvice` + `ResponseBodyAdvice` 适配器 |
| `MvcErrorPackagingActuator` | ErrorController 识别执行器 |
| `UnifiedFailureResponse` | `@Aspect` 全局异常捕获切面 |
| `EnableRsm4Mvc` | 显式启用注解 |
| `MvcRsmAutoConfiguration` | Spring Boot 自动配置 |

## 架构说明

```
Controller 方法返回
    → UnifiedFailureResponse (@Around 切面，捕获异常)
    → JsonResponseBodyPackerMvcAdapter (ResponseBodyAdvice)
        → JsonResponseBodyPacker.pack()
            → ResponsePackagingActuatorManager
                → MvcErrorPackagingActuator (识别 ErrorController)
                → AnnotationPackagingActuator (@ExecutionSuccess 等)
                → DefaultResponsePackagingActuator (兜底)
```
