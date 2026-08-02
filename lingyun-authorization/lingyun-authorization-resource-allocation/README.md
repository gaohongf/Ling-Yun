# lingyun-authorization-resource-allocation

请求分支控制核心——定义 `@Branch` 注解和分支信息模型。

## 解决了什么问题

RESTful 风格下，同一个资源路径（`GET /users`）在不同角色眼中的数据范围和字段往往不同。
通常做法是为每个角色起独立接口（`/admin/users`、`/manager/users`）——路径一多，接口爆炸。

`@Branch` 回归 REST 语义：**同一路径，多套实现，按权限分流**。admin/manager/user 都用 `GET /users`，
权限系统自动决定执行哪个方法。

## 反例——我们不希望你这样写

```java
// 所有角色的逻辑塞进一个方法，if-else 堆成山
@GetMapping("/users")
public Object getUsers() {
    User currentUser = getCurrentUser();
    if (currentUser.hasRole("admin")) {
        return fullDetailList;
    } else if (currentUser.hasRole("manager")) {
        return limitedList;
    } else {
        return basicList;
    }

}
```

问题：单一方法承载过多分支 → 排序靠后的分支每次都要走过前面所有判断 → 新增角色要改核心业务代码 → 无法独立测试。

## 正确用法`

```java
// 每个角色独立的方法体，@Branch 声明分支名和优先级
@Branch(value = "admin", order = 3)
@GetMapping("/users")
public List<User> adminView() { return fullDetailList; }

@Branch(value = "manager", order = 2)
@GetMapping("/users")
public List<User> managerView() { return limitedList; }

@Branch(value = "user", order = 1)
@GetMapping("/users")
public List<User> userView() { return basicList; }
```

用户权限中包含 `GET:/users#manager` → 命中 `managerView()`。
用户权限中包含 `GET:/users#admin` → 命中 `adminView()`。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-authorization-resource-allocation</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 内容

| 组件 | 说明 |
|---|---|
| `@Branch(value, order)` | 标记方法为分支端点，value 为分支名，order 控制优先级（越大越优先） |
| `BranchInfo` | 分支信息 POJO（name + order） |
