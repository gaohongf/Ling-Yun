# Ling-Yun

> Java 基础设施库 —— 统一响应格式、声明式消息管理、认证授权集成

[![Java 17](https://img.shields.io/badge/Java-17-blue)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-red)](https://maven.apache.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-green)](https://spring.io/projects/spring-boot)
[![Version](https://img.shields.io/badge/Version-1.0.1-brightgreen)]()

## 什么是 Ling-Yun

Ling-Yun 帮你把 Controller 的返回值**自动包装**为统一的 JSON 响应体 `{ code, data, msg, type }`，通过注解声明式管理响应消息，并提供开箱即用的无状态 Token 认证。

所有模块引入即用，无需手动添加注解。

## 特色

- **自动包装**：引入 `rsm-mvc-spring-boot-starter`，所有 `@RestController` 返回值自动包装
- **注解驱动**：`@ExecutionSuccess` / `@ExecutionFailed` 声明响应消息，无需手动调用 `R.msg()`
- **内存兜底**：无数据库时自动启用内存存储，零配置即可运行
- **可扩展**：消息存储支持热插拔——MyBatis-Plus / JDBC / 自定义实现可互换
- **认证授权**：无状态 Token + 5 级鉴权 + Dev/Prod 双模式

## 模块一览

| 模块 | 说明 |
|---|---|
| `lingyun-base-rsm` | 响应标准化核心 |
| `rsm-mvc-spring-boot-starter` | MVC 自动适配 |
| `rsm-mybatisplus-spring-boot-starter` | MyBatis-Plus 消息存储 |
| `rsm-jdbc-spring-boot-starter` | Spring Data JDBC 消息存储 |
| `lingyun-authorization-core` | 认证授权契约 |
| `lingyun-authorization-security-spring-boot-starter` | Spring Security 集成 |

## 快速入门

### 引入依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>rsm-mvc-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

### 编写 Controller

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

    // String 需要使用 RString 包装否则无法生效
    // GET /hello/str
    // -> {"code":1002,"data":"Hello World!","msg":"查询成功","type":"SUCCESS"}
    @ExecutionSuccess(GenericRsm.QUERY_SUCCESS)
    @ExecutionFailed(GenericRsm.QUERY_FAILED)
    @GetMapping("/hello/str")
    public RString helloWorld() {
        return RString.warp("Hello World!");
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

### 定义消息

```java
@Component
public class MyRsm extends RsmManager {
    @RsmInfo(template = "你好, {0}!", status = HttpStatus.OK)
    public static final String GREETING = "My_greeting";
}
```

### 按需引入

| 场景 | 引入模块 |
|---|---|
| 只要统一响应格式 | `lingyun-base-rsm` |
| + Spring MVC 自动适配 | `rsm-mvc-spring-boot-starter` |
| + MyBatis-Plus 消息存储 | `rsm-mybatisplus-spring-boot-starter` |
| + Spring Data JDBC 消息存储 | `rsm-jdbc-spring-boot-starter` |
| 只要认证授权接口 | `lingyun-authorization-core` |
| + Spring Security 集成 | `lingyun-authorization-security-spring-boot-starter` |

## 配置参考

```yaml
# RSM
http:
  response:
    packer:
      annotation:
        auto: true
        default-success-message: "Generic_execution_success"
        default-failed-message: "Generic_execution_failed"

# 认证授权
lingyun:
  auth:
    filter:
      token-parse: prod
    custom:
      manager: prod
```

## 版本历史

| 版本 | 日期 | 说明 |
|---|---|---|
| `1.0.1` | 2026-07-26 | 模块 starter 化：引入即自动配置，RString 包装支持 |
| `1.0.0` | 2026-07-26 | 正式版：RSM + 认证授权 |
