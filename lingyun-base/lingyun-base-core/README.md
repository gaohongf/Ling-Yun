# lingyun-base-core

最底层的抽象接口，被所有其他模块依赖。

## 解决了什么问题

在整个项目中统一 `用户身份标识` 和 `请求属性存储` 的概念，让不同模块能基于同一套接口协作而不互相耦合。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-core</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 内容

| 类 | 说明 |
|---|---|
| `IdentifiedUser` | 用户身份标记接口，返回唯一标识 |
| `CustomRequestAttributes<T>` | 基于 `HttpServletRequest` 的请求属性存储抽象 |

## 被谁依赖

- `lingyun-base-rsm`
- `lingyun-authorization-core`
