package com.lingyun.base.rsm.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应消息实体 — 纯 POJO，不耦合任何 ORM。
 * <p>
 * 子模块（如 lingyun-base-rsm-mybatis）可通过继承或组合方式
 * 添加自身 ORM 映射注解（@TableName 等）。
 */
@Data
public class ResponseMessage implements Serializable {
    /** 流水号，由存储层生成 */
    private Integer code;
    /** 消息键，唯一标识一条消息（天然主键），如 "jakarta.validation.constraints.NotNull.message" */
    private String messageKey;
    /** 消息模板，可包含占位符（如 {min}、{max}），响应写入前用参数替换 */
    private String template;
    /** 消息类型，对应 ResponseType 枚举 */
    private String type;
    /** HTTP 响应状态码（如 200、400、500） */
    private Integer responseStatus;
}
