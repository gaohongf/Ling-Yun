# LingYun Base RSM JDBC

> RSM 的 Spring Data JDBC 消息存储实现

## 定位

与 `lingyun-base-rsm-mybatis` 平行的另一种消息存储方案，用于验证 `ResponseMessageService` 接口的 ORM 无关性。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm-jdbc</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 内容

| 类 | 说明 |
|---|---|
| `JdbcResponseMessage` | extends `ResponseMessage` + Spring Data JDBC 注解（`@Table` / `@Id` + `Persistable`） |
| `ResponseMessageRepository` | `CrudRepository<JdbcResponseMessage, String>` |
| `JdbcResponseMessageService` | 实现 `ResponseMessageService`，组合 Repository |

## 设计要点

- **与 MyBatis 实现可互换**：两个实现均可被 `RsmLoader` 透明使用（只依赖 `ResponseMessageService` 接口）
- **`Persistable<String>`**：通过 `isNew()` 判断 INSERT vs UPDATE，以 `messageKey` 为 ID 类型
- **ORM 注解隔离**：`@Id` 标注在 `messageKey`（天然主键），而非自增 `code`

## 何时选用

- 项目使用 Spring Data JDBC 而非 MyBatis-Plus
- 验证 RSM 框架的 ORM 无关性设计
