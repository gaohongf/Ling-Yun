# lingyun-query-base

查询 DSL 核心——用注解把 Request 对象翻译为标准查询条件，Service 只认魔法石不认 HTTP。

## 解决了什么问题

Controller 收到的 Request 对象不能直接丢给 Service——那会让业务层耦合 HTTP 表示模型。
传统做法是手动从 Request 里掏字段、拼 Map、写 if-else 构建查询条件，字段一多代码就膨胀成猫毛团。

`lingyun-query` 用注解声明映射关系，`CriteriaQueryParser` 自动扫描 Request 字段上的注解，
两阶段解析出标准化的 `QueryCondition` 集合。Service 只接收 `CriteriaQuery`，不关心请求从哪来。

## 反例——我们不希望你这样写

```java
// Service 直接吃 Request，每个字段手动掏
public List<User> search(UserSearchRequest request) {
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    if (request.getName() != null) {
        wrapper.like("name", request.getName());
    }
    if (request.getMinAge() != null) {
        wrapper.ge("age", request.getMinAge());
    }
    if (request.getMaxAge() != null) {
        wrapper.le("age", request.getMaxAge());
    }
    if (request.getStatus() != null) {
        wrapper.eq("status", request.getStatus());
    }
    // ... 再加 10 个字段你敢信喵？
    return mapper.selectList(wrapper);
}
```

问题：Service 逐字段掏值 → 字段越多 if-else 越深 → 新增字段要改 Service 代码 → 无法复用 → 猫猫看着都累。

## 正确用法——注解声明 + 魔法石

```java
// 1. 定义标准查询魔法石
public class UserSearchRequest {
    @Like                       // name LIKE '%value%'
    private String name;

    @Ge("age")                  // age >= value（别名映射）
    private Integer minAge;

    @Le("age")                  // age <= value（别名映射）
    private Integer maxAge;

    @Eq                         // status = value
    private Integer status;

    @Scope                      // createTime BETWEEN [start, end]
    private LocalDateTime[] createTime;

    @Asc                        // ORDER BY createTime ASC
    private String createTimeSort;
}
```

```java
// 2. Controller：Request → CriteriaQuery
@PostMapping("/users/search")
public List<User> search(@RequestBody CriteriaQuery<UserSearchRequest> query) {
    return userService.search(query);
}

// 3. Service：只认魔法石
public List<User> search(CriteriaQuery<? extends Serializable> criteria) {
    for (QueryCondition cond : criteria.getConditions()) {
        // cond.toString() → "name LIKE '%zhang%'"
        // cond.toString() → "age >= 18"
    }
}
```

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-query-base</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 注解 → 条件映射表

| 注解 | 条件类 | SQL 语义 | 别名支持 |
|---|---|---|---|
| `@Eq` | `EqCondition` | `field = value` | ✅ |
| `@Like` | `LikeCondition` | `field LIKE '%value%'` | ✅ |
| `@LikeLeft` | `LikeCondition` | `field LIKE '%value'` | ✅ |
| `@LikeRight` | `LikeCondition` | `field LIKE 'value%'` | ✅ |
| `@In` | `InCondition` | `field IN (...)` | ✅ |
| `@NotIn` | `NotInCondition` | `field NOT IN (...)` | ✅ |
| `@IsNull` | `IsNullCondition` | `field IS NULL` | ✅ |
| `@IsNotNull` | `IsNotNullCondition` | `field IS NOT NULL` | ✅ |
| `@Scope` | `ScopeCondition` | `field >/>=/</<=/BETWEEN` | ✅ |
| `@Between` | `ScopeCondition` | `field BETWEEN [x, y]` | ✅ |
| `@Gt` | `ScopeCondition` | `field > value` | ✅ |
| `@Ge` | `ScopeCondition` | `field >= value` | ✅ |
| `@Lt` | `ScopeCondition` | `field < value` | ✅ |
| `@Le` | `ScopeCondition` | `field <= value` | ✅ |
| `@Order` | `OrderCondition` | `ORDER BY field` | ✅ |
| `@Asc` | `OrderCondition` | `ORDER BY field ASC` | ✅ |
| `@Desc` | `OrderCondition` | `ORDER BY field DESC` | ✅ |

## 核心组件

| 组件 | 说明 |
|---|---|
| `CriteriaQuery<T>` | 标准查询魔法石——Service 只认这个接口 |
| `OrdinaryCriteriaQuery<T>` | 基于 `ObjectMapper` 的默认魔法石实现 |
| `CriteriaQueryParser` | 两阶段扫描解析器：omit-value 注解 → value-bearing 注解 |
| `QueryAnnotation` | 核心元注解——把业务注解映射到条件类 |
| `QueryConditionUtils` | 条件工厂——注解元数据 → `QueryCondition` 实例 |
| `ConditionNode` | 魔力导线——用 AND/OR 编织嵌套条件 |

## 核心流程

```
Request JSON → ObjectMapper 反序列化 → OrdinaryCriteriaQuery
  → CriteriaQueryParser.parse()
    → 阶段一：扫描 @OmitValueClause 注解（@Asc/@Desc/@IsNull/@IsNotNull）
    → 阶段二：扫描有值字段的注解（@Eq/@Like/@Scope...）
      → QueryConditionUtils.getCondition()
        → new EqCondition() / new LikeCondition() / ...
    → setFieldName + setValue
  → 返回标准化的 Collection<QueryCondition>
  → Service / 方言层渲染为 SQL
```

## 架构

```
lingyun-query-base
├── annotation/         查询注解声明（@Eq, @Like, @Scope...）
├── condition/          条件模型（EqCondition, LikeCondition, ConditionNode...）
└── criteria/           魔法石 + 解析器（CriteriaQuery, CriteriaQueryParser）
```
