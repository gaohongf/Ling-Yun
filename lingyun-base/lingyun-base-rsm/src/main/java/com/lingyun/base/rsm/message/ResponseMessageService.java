package com.lingyun.base.rsm.message;

import java.util.List;
import java.util.Optional;

/**
 * 响应消息存储抽象 — 不绑定任何 ORM 或存储方案。
 * <p>
 * 项目通过实现此接口接入具体的存储层（MyBatis、JPA、Redis、配置中心等）。
 * lingyun-base-rsm-mybatis 提供了一个基于 MyBatis-Plus 的默认实现。
 */
public interface ResponseMessageService {

    /**
     * 根据消息键查询消息实体。
     *
     * @param key 消息键（messageKey）
     * @return 对应的 ResponseMessage，未找到时返回 null
     */
    ResponseMessage findByMessageKey(String key);

    /**
     * 根据消息键查询消息实体，返回 Optional 包装。
     *
     * @param key 消息键（messageKey）
     * @return 包含 ResponseMessage 的 Optional，未找到时为 Optional.empty()
     */
    default Optional<ResponseMessage> findOptByMessageKey(String key) {
        return Optional.ofNullable(findByMessageKey(key));
    }

    /**
     * 获取全部消息实体列表。
     *
     * @return 所有 ResponseMessage 的列表
     */
    List<ResponseMessage> list();

    /**
     * 保存一条消息实体。
     *
     * @param message 待保存的消息实体
     * @return 保存成功返回 true，否则返回 false
     */
    boolean save(ResponseMessage message);

    /**
     * 根据消息键（messageKey）更新消息模板、类型和状态码。
     *
     * @param message 携带更新后字段的消息实体（以 messageKey 作为匹配条件）
     * @return 更新成功返回 true，否则返回 false
     */
    boolean updateByKey(ResponseMessage message);
}
