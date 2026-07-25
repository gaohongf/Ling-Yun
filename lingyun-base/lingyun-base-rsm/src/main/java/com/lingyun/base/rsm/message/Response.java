package com.lingyun.base.rsm.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 默认响应体 — RSM 框架的建议实现。
 * <p>
 * 项目可通过实现 {@code ResponseBuilder<MyResponse>} 替换为此类以外的响应结构。
 */
@Data
public class Response implements Serializable {
    /** HTTP 状态码 */
    private Integer code;
    /** 响应携带的业务数据，可为任意类型 */
    private Object data;
    /** 响应消息文本（已解析的消息模板） */
    private String msg;
    /** 响应类型（SUCCESS / WARN / INFO / ERROR） */
    private ResponseType type;
}
