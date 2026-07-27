# rsm-mvc-spring-boot-starter

将 RSM 接入 Spring Web MVC，引入即自动包装 Controller 返回值。

## 解决了什么问题

引入 `lingyun-base-rsm` 后 Controller 返回值不会被自动包装——因为核心包不依赖 MVC。加上这个 Starter，所有 `@RestController` 的返回值自动走 RSM 包装链路。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>rsm-mvc-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

依赖：`lingyun-base-rsm` + `spring-boot-starter-web`，自动引入无需额外配置。

## 内容

| 组件 | 说明 |
|---|---|
| `JsonResponseBodyPackerMvcAdapter` | `ResponseBodyAdvice` 适配器，自动拦截返回值 |
| `UnifiedFailureResponse` | `@Aspect` 切面，未处理异常自动转换为 RSM 错误响应 |
| `MvcErrorPackagingActuator` | Spring Boot ErrorController 错误页识别 |
