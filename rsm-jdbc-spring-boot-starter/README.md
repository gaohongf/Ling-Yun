# rsm-jdbc-spring-boot-starter

RSM 的 Spring Data JDBC 消息存储实现——与 MyBatis-Plus 版功能等价，适用于使用 Spring Data JDBC 的项目。

## 解决了什么问题

与 `rsm-mybatisplus-spring-boot-starter` 解决的问题相同：将消息模板持久化。选择这个版本而非 MyBatis-Plus 版的唯一原因就是你的项目用的是 Spring Data JDBC。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>rsm-jdbc-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

依赖：`lingyun-base-rsm` + `spring-boot-starter-data-jdbc`

## 内容

| 组件 | 说明 |
|---|---|
| `JdbcResponseMessageService` | `ResponseMessageService` 的 JDBC 实现 |
| `JdbcResponseMessage` | 带 `@Table` / `@Id` 的消息实体 |
| `RsmJdbcPluginAutoConfiguration` | 自动配置，引入即替换内存存储 |
