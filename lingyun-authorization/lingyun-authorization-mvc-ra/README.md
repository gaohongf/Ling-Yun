# lingyun-authorization-mvc-ra

请求分支控制的 Spring MVC 适配——让 `@Branch` 在 Spring MVC 中生效。

## 解决了什么问题

`lingyun-authorization-resource-allocation` 只定义了 `@Branch` 注解和 `BranchInfo` 模型，
要让它在 Spring MVC 的请求映射链路中真正影响方法选择，需要自定义 `RequestMappingHandlerMapping`
和 `RequestCondition`。本模块封装了这些 MVC 集成细节，引入即生效。

## 依赖

```xml
<dependency>
    <groupId>com.lingyun</groupId>
    <artifactId>lingyun-authorization-mvc-ra</artifactId>
    <version>1.0.1</version>
</dependency>
```

依赖：`lingyun-authorization-resource-allocation` + `lingyun-authorization-core` + `spring-boot-starter-web`

## 工作流程

```
请求 GET /users
  → BranchRequestMappingHandlerMapping（替换默认）
    → 扫描同一路径的所有方法 → 按 @Branch.order 降序排列
    → 逐一执行 BranchRequestCondition.getMatchingCondition()
      → BranchAuthorizationMatcher.match()
        → 读取当前 ResourceInfo.id() → 构造分支权限标识 GET:/users#admin
        → 检查用户权限集合是否包含该标识
        → 有 → 匹配成功，执行对应方法
        → 无 → 继续下一个 order 的分支
    → 全部不匹配 → 命中 order 最低的方法兜底
```

## 内容

| 组件 | 说明 |
|---|---|
| `BranchRequestMappingHandlerMapping` | 替换默认 `RequestMappingHandlerMapping`，识别 `@Branch` |
| `BranchRequestCondition` | `RequestCondition` 实现，注入分支权限检查 |
| `BranchAuthorizationMatcher` | 检查用户是否有权访问指定分支 |
| `BranchResourceInfoBuilder` | 构建带分支的资源 ID（`GET:/path#branchName`） |
| `MvcRaAutoConfiguration` | 自动配置，引入即生效 |
