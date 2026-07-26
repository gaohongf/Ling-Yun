# LingYun Base Core

> 纯 JDK 基础设施核心 —— 零框架依赖

## 定位

`lingyun-base-core` 是整个 Ling-Yun 生态的**最底层模块**，仅依赖 JDK 17 + Lombok（compile-time），不引入任何框架依赖。它提供整个项目共用的基础抽象接口。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-core</artifactId>
    <version>1.0.1</version>
</dependency>
```

传递依赖：**无**（仅 Lombok `optional`）

## 内容

| 包 | 类 | 说明 |
|---|---|---|
| `com.lingyun.base.user` | `IdentifiedUser` | 已认证用户的身份标记接口，返回 `Serializable getId()` |
| `com.lingyun.base.request` | `CustomRequestAttributes<T>` | 基于 `HttpServletRequest.setAttribute` 的请求级属性存储抽象基类 |

## 设计原则

- 不依赖 Spring、不依赖任何框架
- 只定义契约（接口/抽象类），具体实现由上层模块提供
- 被所有其他 LingYun 模块共同依赖
