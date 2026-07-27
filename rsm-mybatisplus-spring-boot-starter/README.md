# rsm-mybatisplus-spring-boot-starter

RSM 的 MyBatis-Plus 消息存储实现——将消息模板持久化到数据库。

## 解决了什么问题

默认内存存储的消息在应用重启后会丢失。引入此模块后，消息模板写入数据库，应用重启后依然可用。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>rsm-mybatisplus-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

依赖：`lingyun-base-rsm` + `mybatis-plus-spring-boot3-starter`

## 内容

| 组件 | 说明 |
|---|---|
| `MybatisResponseMessageService` | `ResponseMessageService` 的 MyBatis-Plus 实现 |
| `MpResponseMessage` | 带 `@TableName` / `@TableId` 的消息实体 |
| `RsmMybatisPlusPluginAutoConfiguration` | 自动配置，引入即替换内存存储 |

### 建表 SQL

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
