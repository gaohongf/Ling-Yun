# RSM MyBatis-Plus Spring Boot Starter

> Spring Boot Starter — RSM 的 MyBatis-Plus 消息存储，引入即自动配置

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>rsm-mybatisplus-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 自动配置

引入后通过 `AutoConfiguration.imports` 自动加载 `RsmMybatisPlusPluginAutoConfiguration`，注册 `MybatisResponseMessageService` 替代默认内存实现。

## 内容

| 类 | 说明 |
|---|---|
| `RsmMybatisPlusPluginAutoConfiguration` | 自动配置入口（含 `@EnableRsm`） |
| `MpResponseMessage` | extends `ResponseMessage` + `@TableName` / `@TableId` |
| `ResponseMessageMapper` | MyBatis-Plus `BaseMapper<MpResponseMessage>` |
| `MybatisResponseMessageService` | 实现 `ResponseMessageService`，组合 Mapper |

## 设计要点

- **ORM 注解隔离**：`MpResponseMessage`  extends `ResponseMessage`（纯 POJO）
- **组合优于继承**：不继承 `ServiceImpl`，避免泛型 diamond 问题
- **`messageKey` 为主键**：`@TableId` 标注在 `messageKey`（天然主键）

## 表结构

```sql
CREATE TABLE response_message (
    code            INT AUTO_INCREMENT,
    message_key     VARCHAR(128) PRIMARY KEY,
    template        VARCHAR(512),
    type            VARCHAR(32),
    response_status INT,
    INDEX idx_type (type)
);
```
