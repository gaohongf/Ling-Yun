# LingYun Base

> 基础设施聚合模块

## 子模块

| 模块 | 说明 | 框架依赖 |
|---|---|---|
| [lingyun-base-core](lingyun-base-core/) | 纯 JDK 核心接口 | **无** |
| [lingyun-base-rsm](lingyun-base-rsm/) | 响应标准化框架 | Spring Web |
| [rsm-mvc-spring-boot-starter](../rsm-mvc-spring-boot-starter/) | MVC 适配（自动配置） | Spring Web MVC |
| [rsm-mybatisplus-spring-boot-starter](../rsm-mybatisplus-spring-boot-starter/) | MyBatis-Plus 存储（自动配置） | MyBatis-Plus |
| [rsm-jdbc-spring-boot-starter](../rsm-jdbc-spring-boot-starter/) | JDBC 存储（自动配置） | Spring Data JDBC |

## 依赖层次

```
lingyun-base-core           ← 零框架依赖
    ↑
lingyun-base-rsm            ← + Spring Web
    ↑
    ├── rsm-mvc-spring-boot-starter        ← + MVC（自动加载）
    ├── rsm-mybatisplus-spring-boot-starter ← + MyBatis-Plus（自动加载）
    └── rsm-jdbc-spring-boot-starter       ← + JDBC（自动加载）
```

## 引入

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base</artifactId>
    <version>1.0.1</version>
    <type>pom</type>
</dependency>
```
