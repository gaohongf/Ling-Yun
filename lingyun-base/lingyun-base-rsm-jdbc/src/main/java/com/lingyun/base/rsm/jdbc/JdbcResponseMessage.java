package com.lingyun.base.rsm.jdbc;

import com.lingyun.base.rsm.message.ResponseMessage;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * ResponseMessage 的 Spring Data JDBC 实体扩展。
 * <p>
 * 添加 {@code @Table} 和 {@code @Id} 注解以使 Spring Data JDBC 能正确映射。
 * messageKey 是响应消息的天然主键，code 是由 RsmLoader 自动分配的流水号。
 * 实现 {@link Persistable} 以精确控制 INSERT vs UPDATE 行为。
 * <p>
 * 不暴露给模块外部——{@link JdbcResponseMessageService} 在内部使用它。
 */
@Table("response_message")
public class JdbcResponseMessage extends ResponseMessage implements Persistable<String> {

    /** 复写父类字段以添加 {@link Id}——messageKey 是天然主键 */
    @Id
    private String messageKey;

    /** 标记是否为新建实体，用于 {@link #isNew()} */
    @Transient
    private boolean isNew;

    /** Spring Data JDBC 加载数据库记录时使用的无参构造器。加载后 isNew=false。 */
    public JdbcResponseMessage() {
        this.isNew = false;
    }

    /**
     * 从 {@link ResponseMessage} 构造——用于新建或更新操作。
     *
     * @param source 源消息对象
     */
    public JdbcResponseMessage(ResponseMessage source) {
        this.setCode(source.getCode());
        this.messageKey = source.getMessageKey();
        this.setTemplate(source.getTemplate());
        this.setType(source.getType());
        this.setResponseStatus(source.getResponseStatus());
        this.isNew = source.getMessageKey() == null;
    }

    // ---- 字段读写 ----

    /**
     * 获取消息键——复写父类方法以返回带有 {@code @Id} 注解的字段值。
     *
     * @return messageKey
     */
    @Override
    public String getMessageKey() {
        return this.messageKey;
    }

    /**
     * 设置消息键——复写父类方法以设置带有 {@code @Id} 注解的字段值。
     *
     * @param messageKey 消息键
     */
    @Override
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    // ---- Persistable<String> ----

    /**
     * 返回实体 ID——此处即 messageKey，Spring Data 据此判断主键值。
     *
     * @return messageKey
     */
    @Override
    @Transient
    public String getId() {
        return getMessageKey();
    }

    /**
     * 判断实体是否为新建——Spring Data 据此决定执行 INSERT 还是 UPDATE。
     *
     * @return true — 新建实体（将执行 INSERT）；false — 已存在实体（将执行 UPDATE）
     */
    @Override
    @Transient
    public boolean isNew() {
        return isNew;
    }

    /** 标记为新建（下次 save 执行 INSERT） */
    public void markNew() {
        this.isNew = true;
    }

    /** 标记为已存在（下次 save 执行 UPDATE） */
    public void markNotNew() {
        this.isNew = false;
    }
}
