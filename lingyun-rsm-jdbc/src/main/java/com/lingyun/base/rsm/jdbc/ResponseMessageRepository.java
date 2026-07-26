package com.lingyun.base.rsm.jdbc;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lingyun.base.rsm.message.ResponseMessage;

/**
 * {@link JdbcResponseMessage} 的 Spring Data JDBC 仓库。
 * <p>
 * ID 类型为 String（对应 messageKey 天然主键）。
 * {@code findById(messageKey)} 等价于 {@code WHERE message_key = ?}。
 */
@Repository
public interface ResponseMessageRepository extends CrudRepository<JdbcResponseMessage, String> {
    
}
