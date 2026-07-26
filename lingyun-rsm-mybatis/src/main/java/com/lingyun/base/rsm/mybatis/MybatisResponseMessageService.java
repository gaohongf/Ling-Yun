package com.lingyun.base.rsm.mybatis;

import com.lingyun.base.rsm.message.ResponseMessage;
import com.lingyun.base.rsm.message.ResponseMessageService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * ResponseMessageService 的 MyBatis-Plus 实现 — 组合 Mapper 而非继承 ServiceImpl，避免泛型 diamond 问题。
 * <p>
 * 内部使用 {@link MpResponseMessage} 承载 MyBatis-Plus 注解（@TableName, @TableId）。
 * messageKey 是天然主键——{@code selectById(key)} 等价于 {@code WHERE message_key = key}。
 */
@Service
public class MybatisResponseMessageService implements ResponseMessageService {

    private final ResponseMessageMapper mapper;

    /**
     * 通过构造注入 {@link ResponseMessageMapper}，组合而非继承。
     *
     * @param mapper MyBatis-Plus BaseMapper
     */
    public MybatisResponseMessageService(ResponseMessageMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据消息键查找消息——messageKey 是天然主键，使用 {@code selectById} 等价于 WHERE message_key = ?。
     *
     * @param key 消息键
     * @return 匹配的 ResponseMessage，不存在时返回 null
     */
    @Override
    public ResponseMessage findByMessageKey(String key) {
        return mapper.selectById(key);
    }

    /**
     * 列出全部响应消息——返回不可变副本以防止外部修改。
     *
     * @return 全部 ResponseMessage 列表
     */
    @Override
    public List<ResponseMessage> list() {
        return List.copyOf(mapper.selectList(null));
    }

    /**
     * 新增一条响应消息。
     *
     * @param message 待保存的消息
     * @return true — 保存成功
     */
    @Override
    public boolean save(ResponseMessage message) {
        return mapper.insert(ensureEntity(message)) > 0;
    }

    /**
     * 根据消息键更新一条响应消息（WHERE message_key = ?）。
     *
     * @param message 待更新的消息
     * @return true — 更新成功
     */
    @Override
    public boolean updateByKey(ResponseMessage message) {
        return mapper.updateById(ensureEntity(message)) > 0;
    }

    /** 若传入的已是实体则直接使用，否则新建包装。 */
    private MpResponseMessage ensureEntity(ResponseMessage message) {
        if (message instanceof MpResponseMessage entity) {
            return entity;
        }
        return new MpResponseMessage(message);
    }

    @Override
    public void batchSaveOrUpdate(Collection<ResponseMessage> messages) {
        mapper.insertOrUpdate(messages.stream().map(this::ensureEntity).toList());
    }
}
