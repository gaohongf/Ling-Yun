# LingYun Base

> 基础设施聚合模块

## 子模块

| 模块 | 说明 | 框架依赖 |
|---|---|---|
| [lingyun-base-core](lingyun-base-core/) | 纯 JDK 核心接口 | **无** |
| [lingyun-base-rsm](lingyun-base-rsm/) | 响应标准化框架 | Spring Web（不含 MVC） |
| [lingyun-base-rsm-mvc](lingyun-base-rsm-mvc/) | RSM 的 MVC 适配层 | Spring Web MVC |
| [lingyun-base-rsm-mybatis](lingyun-base-rsm-mybatis/) | RSM 的 MyBatis-Plus 存储 | MyBatis-Plus |
| [lingyun-base-rsm-jdbc](lingyun-base-rsm-jdbc/) | RSM 的 Spring Data JDBC 存储 | Spring Data JDBC |

## 依赖层次

```
lingyun-base-core           ← 零框架依赖
    ↑
lingyun-base-rsm            ← + Spring Web
    ↑
    ├── lingyun-base-rsm-mvc     ← + Spring Web MVC
    ├── lingyun-base-rsm-mybatis ← + MyBatis-Plus
    └── lingyun-base-rsm-jdbc    ← + Spring Data JDBC
```

## 引入

```xml
<!-- 父 POM -->
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base</artifactId>
    <version>1.0.0</version>
    <type>pom</type>
</dependency>
```
