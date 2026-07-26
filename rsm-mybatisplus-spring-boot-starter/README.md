# LingYun Base RSM MyBatis

> RSM 的 MyBatis-Plus 消息存储实现

## 定位

为 RSM 框架提供基于 MyBatis-Plus 的 `ResponseMessageService` 实现，将消息模板持久化到数据库。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm-mybatis</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 内容

| 类 | 说明 |
|---|---|
| `MpResponseMessage` | extends `ResponseMessage` + MyBatis-Plus 注解（`@TableName` / `@TableId`） |
| `ResponseMessageMapper` | MyBatis-Plus `BaseMapper<MpResponseMessage>` |
| `MybatisResponseMessageService` | 实现 `ResponseMessageService`，组合 Mapper 而非继承 `ServiceImpl` |

## 设计要点

- **组合优于继承**：`MybatisResponseMessageService` 组合 `ResponseMessageMapper`，不继承 `ServiceImpl`，避免泛型 diamond 问题
- **ORM 注解隔离**：`MpResponseMessage`  extends `ResponseMessage`（纯 POJO），ORM 注解仅在子类中声明
- **`messageKey` 为主键**：`@TableId` 标注在 `messageKey` 字段（天然主键），而非自增 `code`

## 表结构

建表 SQL（MySQL）：

```sql
CREATE TABLE response_message (
    code          INT AUTO_INCREMENT,
    message_key   VARCHAR(128) PRIMARY KEY,
    template      VARCHAR(512),
    type          VARCHAR(32),
    response_status INT,
    INDEX idx_type (type)
);
```
