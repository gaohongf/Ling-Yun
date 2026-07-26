package com.lingyun.base.rsm.jdbc;

import com.lingyun.base.rsm.message.ResponseMessage;
import com.lingyun.base.rsm.message.ResponseMessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link ResponseMessageService} 的 Spring Data JDBC 实现。
 * <p>
 * 组合 {@link ResponseMessageRepository}（CrudRepository），
 * messageKey 是天然主键——{@code findById(key)} 等价于 {@code WHERE message_key = ?}。
 * <p>
 * 设计要点：
 * <ul>
 * <li>使用 {@link JdbcResponseMessage} 作为内部实体，实现 {@code Persistable<String>}
 * 以精确控制 INSERT/UPDATE</li>
 * <li>{@code save()} → INSERT（强制）</li>
 * <li>{@code updateByKey()} → UPDATE（强制，WHERE message_key = ?）</li>
 * <li>返回值始终为 {@link ResponseMessage}，保持接口隔离</li>
 * </ul>
 */
@Service
public class JdbcResponseMessageService implements ResponseMessageService {

    private final ResponseMessageRepository repository;

    /**
     * 通过构造注入 {@link ResponseMessageRepository}，组合而非继承。
     *
     * @param repository Spring Data JDBC CrudRepository
     */
    public JdbcResponseMessageService(ResponseMessageRepository repository) {
        this.repository = repository;
    }

    /**
     * 根据消息键查找消息——messageKey 是天然主键，使用 {@code findById(key)} 等价于 WHERE message_key =
     * ?。
     *
     * @param key 消息键
     * @return 匹配的 ResponseMessage，不存在时返回 null
     */
    @Override
    public ResponseMessage findByMessageKey(String key) {
        return repository.findById(key).orElse(null);
    }

    /**
     * 列出全部响应消息。
     *
     * @return 全部 ResponseMessage 列表
     */
    @Override
    public List<ResponseMessage> list() {
        List<ResponseMessage> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    /**
     * 新增一条响应消息——通过 {@link JdbcResponseMessage#markNew()} 强制 INSERT。
     * 
     * @param message 待保存的消息
     * @return true — 保存成功
     */
    @Override
    public boolean save(ResponseMessage message) {
        if (repository.existsById(message.getMessageKey())) {
            return false;
        }

        JdbcResponseMessage entity = ensureEntity(message);
        entity.markNew(); // 强制 INSERT
        repository.save(entity);
        return true;
    }

    /**
     * 根据消息键更新一条响应消息——通过 {@link JdbcResponseMessage#markNotNew()} 强制 UPDATE（WHERE
     * message_key = ?）。
     *
     * @param message 待更新的消息
     * @return true — 更新成功
     */
    @Override
    public boolean updateByKey(ResponseMessage message) {
        JdbcResponseMessage entity = ensureEntity(message);
        entity.markNotNew(); // 强制 UPDATE（WHERE message_key = ?）
        repository.save(entity);
        return true;
    }

    @Override
    public void batchSaveOrUpdate(Collection<ResponseMessage> messages) {
        repository.saveAll(messages.stream()
                .map(this::ensureEntity)
                .peek(msg -> {
                    if (!repository.existsById(msg.getMessageKey())) {
                        msg.markNew();
                    } else {
                        msg.markNotNew();
                    }
                })
                .toList());
    }

    /** 若传入的已是实体则直接使用，否则新建包装。 */
    private JdbcResponseMessage ensureEntity(ResponseMessage message) {
        if (message instanceof JdbcResponseMessage entity) {
            return entity;
        }
        return new JdbcResponseMessage(message);
    }
}
