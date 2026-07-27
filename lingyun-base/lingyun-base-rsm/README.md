# lingyun-base-rsm

响应标准化核心——提供统一响应体、消息管理、注解驱动包装。

## 解决了什么问题

不用在每个 Controller 方法里手动构建 `{ code, data, msg }` 响应体。声明注解 + 配置消息模板，框架自动完成包装。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-base-rsm</artifactId>
    <version>1.0.1</version>
</dependency>
```

依赖：`lingyun-base-core` + `spring-web` + `Jackson` + `Hibernate Validator`

## 反例——我们不希望你这样写

```java
// 每个方法手动 new HashMap，塞 code、msg、data
@GetMapping("/user")
public Map<String, Object> getUser() {
    Map<String, Object> result = new HashMap<>();
    result.put("code", 200);
    result.put("msg", "查询成功");
    result.put("data", userService.findById(1));
    return result;
}

// 失败时又要重复一遍，code 和 msg 散落一地
@GetMapping("/order")
public Map<String, Object> getOrder() {
    Map<String, Object> result = new HashMap<>();
    try {
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", orderService.findById(1));
    } catch (Exception e) {
        result.put("code", 500);
        result.put("msg", "查询失败");
        result.put("data", null);
    }
    return result;
}
```

这样写的问题：响应结构不统一、code/msg 散落各处、没有类型约束、消息文本硬编码无法复用。

## 正确用法——交给框架

```java
// 返回业务对象即可，框架自动包装
@ExecutionSuccess(GenericRsm.QUERY_SUCCESS)
@ExecutionFailed(GenericRsm.QUERY_FAILED)
@GetMapping("/user")
public User getUser() {
    return userService.findById(1);
}
// -> {"code":1002,"data":{"id":1,"name":"zhangsan"},"msg":"查询成功","type":"SUCCESS"}

// 失败自动走 @ExecutionFailed 声明
@ExecutionSuccess(GenericRsm.QUERY_SUCCESS)
@ExecutionFailed(GenericRsm.QUERY_FAILED)
@GetMapping("/order")
public Order getOrder() {
    return orderService.findById(1);
}
// 抛出异常时 -> {"code":1003,"data":null,"msg":"查询失败","type":"ERROR"}
```

## 内容

| 特性 | 说明 |
|---|---|
| 统一响应体 | `Response { code, data, msg, type }` |
| 静态工具类 | `R.msg()` / `R.error()` 快捷构建 |
| 注解声明 | `@RsmInfo` 声明消息模板，启动时自动同步 |
| 包装控制 | `@ExecutionSuccess` / `@ExecutionFailed` / `@NotPack` |
| 内存存储 | `DefaultResponseMessageServiceImpl` 无数据库时兜底 |
