# RSM MVC Spring Boot Starter

> Spring Boot Starter — 引入即自动启用 RSM + Spring Web MVC 适配

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>rsm-mvc-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 自动配置

引入后通过 `AutoConfiguration.imports` 自动加载 `MvcRsmAutoConfiguration`，无需添加任何注解。自动注册：

- `JsonResponseBodyPackerMvcAdapter` — `@ControllerAdvice` + `ResponseBodyAdvice` 自动包装
- `UnifiedFailureResponse` — `@Aspect` 全局异常捕获 → RSM 错误响应
- `MvcErrorPackagingActuator` — ErrorController 错误路径识别

## 内容

| 类 | 说明 |
|---|---|
| `MvcRsmAutoConfiguration` | 自动配置入口（含 `@EnableRsm`） |
| `JsonResponseBodyPackerMvcAdapter` | `ResponseBodyAdvice` 适配器 |
| `UnifiedFailureResponse` | `@Aspect` 异常捕获切面 |
| `MvcErrorPackagingActuator` | ErrorController 包装执行器 |
| `@EnableRsm4Mvc` | 可选显式声明注解 |

## 响应包装链路

```
Controller 方法返回
    → UnifiedFailureResponse (@Around 切面)
    → JsonResponseBodyPackerMvcAdapter (ResponseBodyAdvice)
        → JsonResponseBodyPacker.pack()
            → ResponsePackagingActuatorManager
                → AnnotationPackagingActuator
                → DefaultResponsePackagingActuator
```
