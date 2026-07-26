package com.lingyun.base.rsm.mybatis;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lingyun.base.rsm.message.ResponseMessage;

/**
 * ResponseMessage 的 MyBatis-Plus 实体扩展。
 * <p>
 * 核心 {@link ResponseMessage} 是纯 POJO，不携带 ORM 注解。
 * 此处通过字段复写在 {@code messageKey} 上添加 {@code @TableId}——
 * messageKey 是响应消息的天然主键，code 是由 RsmLoader 自动分配的流水号。
 * <p>
 * 不暴露给模块外部——{@link MybatisResponseMessageService} 在内部使用它。
 */
@TableName("response_message")
public class MpResponseMessage extends ResponseMessage {

    /** 复写父类字段以添加 {@link TableId}——messageKey 是天然主键 */
    @TableId
    private String messageKey;

    /** MyBatis-Plus 反序列化使用的无参构造器 */
    public MpResponseMessage() {
    }

    /**
     * 从 {@link ResponseMessage} 构造。
     *
     * @param source 源消息对象
     */
    public MpResponseMessage(ResponseMessage source) {
        this.setCode(source.getCode());
        this.messageKey = source.getMessageKey();
        this.setTemplate(source.getTemplate());
        this.setType(source.getType());
        this.setResponseStatus(source.getResponseStatus());
    }

    /**
     * 获取消息键——复写父类方法以返回带有 {@code @TableId} 注解的字段值。
     *
     * @return messageKey
     */
    @Override
    public String getMessageKey() {
        return this.messageKey;
    }

    /**
     * 设置消息键——复写父类方法以设置带有 {@code @TableId} 注解的字段值。
     *
     * @param messageKey 消息键
     */
    @Override
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
