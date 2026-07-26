# RSM JDBC Spring Boot Starter

> Spring Boot Starter — RSM 的 Spring Data JDBC 消息存储，引入即自动配置

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>rsm-jdbc-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 自动配置

引入后通过 `AutoConfiguration.imports` 自动加载 `RsmJdbcPluginAutoConfiguration`，注册 `JdbcResponseMessageService` 替代默认内存实现。

## 内容

| 类 | 说明 |
|---|---|
| `RsmJdbcPluginAutoConfiguration` | 自动配置入口（含 `@EnableRsm`） |
| `JdbcResponseMessage` | extends `ResponseMessage` + `@Table` / `@Id` + `Persistable` |
| `ResponseMessageRepository` | `CrudRepository<JdbcResponseMessage, String>` |
| `JdbcResponseMessageService` | 实现 `ResponseMessageService`，组合 Repository |

## 设计要点

- 与 MyBatis-Plus 实现可互换，均实现 `ResponseMessageService` 接口
- `Persistable<String>` 控制 INSERT vs UPDATE
- **`messageKey` 为主键**：`@Id` 标注在 `messageKey`（天然主键）
