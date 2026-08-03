package com.lingyun.query.criteria;

import java.io.Serializable;
import java.util.Collection;

import com.lingyun.query.condition.QueryCondition;

/**
 * 标准查询魔法石（Criteria）——Controller 与 Service 之间的查询契约。
 * <p>
 * 禁止把 Controller 里收到的 XXRequest 对象直接丢给 Service，那会让 Service 耦合
 * HTTP 层的表示模型，在分布式猫猫网络中尤其容易爆炸喵。
 * <p>
 * 正确做法：Controller 收到 Request 后，将其翻译为 {@link CriteriaQuery}，
 * Service 只认这个标准查询接口，不关心原始请求长什么样。
 *
 * <h3>使用示例</h3>
 * 
 * <pre>{@code
 * // Controller 层：Request → CriteriaQuery
 * @PostMapping("/users/search")
 * public List<User> search(@RequestBody UserSearchRequest request) {
 *     UserCriteria criteria = new UserCriteria(request);
 *     return userService.search(criteria);
 * }
 *
 * // Service 层：只认 CriteriaQuery
 * public List<User> search(CriteriaQuery<UserSearchRequest> criteria) {
 *     // 可以取原始请求
 *     UserSearchRequest raw = criteria.getRaw();
 *     // 也可以直接取标准化的查询条件
 *     for (QueryCondition cond : criteria.getConditions()) {
 *         // 构建 SQL / QueryWrapper
 *     }
 * }
 * }</pre>
 *
 * @param <T> 原始请求类型，通过 {@link #getRaw()} 可取出完整的原始对象
 * @see QueryCondition
 */
public interface CriteriaQuery<T extends Serializable> {

    /**
     * 取出原始请求对象。
     * <p>
     * 保留原始数据是为了处理那些无法自动翻译为标准 {@link QueryCondition} 的特殊字段。
     * 大多数情况下应该优先使用 {@link #getConditions()}。
     *
     * @return 原始的请求对象，不应为 {@code null}
     */
    T getRaw();

    /**
     * 获取翻译后的标准查询条件集合。
     * <p>
     * 这是框架真正关心的部分——每个 {@link QueryCondition} 对应一个 SQL 子句
     * （如 {@code =}、{@code LIKE}、{@code BETWEEN} 等），由方言层负责渲染。
     *
     * @return 查询条件集合，无条件时返回空集合
     */
    Collection<? extends QueryCondition> getConditions();

    /**
     * 写入查询条件
     */
    void setConditions(Collection<? extends QueryCondition> conditions);

}
