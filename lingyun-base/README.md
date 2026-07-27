# lingyun-base

基础设施聚合模块。

## 子模块

| 模块 | artifactId | 说明 |
|---|---|---|
| [lingyun-base-core](lingyun-base-core/) | `lingyun-base-core` | 核心接口 |
| [lingyun-base-rsm](lingyun-base-rsm/) | `lingyun-base-rsm` | 响应标准化 |
| [rsm-mvc-spring-boot-starter](../rsm-mvc-spring-boot-starter/) | `rsm-mvc-spring-boot-starter` | MVC 适配 |
| [rsm-mybatisplus-spring-boot-starter](../rsm-mybatisplus-spring-boot-starter/) | `rsm-mybatisplus-spring-boot-starter` | MyBatis-Plus 存储 |
| [rsm-jdbc-spring-boot-starter](../rsm-jdbc-spring-boot-starter/) | `rsm-jdbc-spring-boot-starter` | JDBC 存储 |

## 依赖层次

```
lingyun-base-core
    ↑
lingyun-base-rsm
    ↑
    ├── rsm-mvc-spring-boot-starter
    ├── rsm-mybatisplus-spring-boot-starter
    └── rsm-jdbc-spring-boot-starter
```
