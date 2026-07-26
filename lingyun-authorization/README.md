# LingYun Authorization

> 认证授权聚合模块

## 子模块

| 模块 | 说明 | 框架依赖 |
|---|---|---|
| [lingyun-authorization-core](lingyun-authorization-core/) | 认证授权核心契约 | **无** |
| [lingyun-authorization-security](lingyun-authorization-security/) | Spring Security 集成（artifactId 含 `-spring-boot-starter`） | Spring Security |

## 依赖层次

```
lingyun-base-core                  ← 零框架依赖
    ↑
lingyun-authorization-core         ← 零框架依赖（仅依赖 base-core）
    ↑
lingyun-authorization-security-spring-boot-starter  ← Spring Security + lingyun-base-rsm
```

## 引入

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-authorization</artifactId>
    <version>1.0.1</version>
    <type>pom</type>
</dependency>
```
