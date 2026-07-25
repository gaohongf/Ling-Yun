-- ============================================================
-- Ling-Yun RSM 响应消息表
-- 适用于: lingyun-base-rsm-mybatis / lingyun-base-rsm-jdbc
-- 版本: 1.0.0
-- ============================================================

CREATE TABLE IF NOT EXISTS response_message (
    -- 流水号，由数据库自动生成，仅用于排序和引用
    code            INT             NOT NULL AUTO_INCREMENT  COMMENT '流水号',
    -- 消息键，唯一标识一条消息（天然主键），由 @RsmInfo 声明时指定
    -- 格式示例: "Generic_execution_success"、"jakarta.validation.constraints.NotNull.message"
    message_key     VARCHAR(128)    NOT NULL                 COMMENT '消息键',
    -- 消息模板，可包含 MessageFormat 占位符（如 {0}、{min}）
    -- 响应写入时用实际参数替换
    template        VARCHAR(512)    NOT NULL                 COMMENT '消息模板',
    -- 消息类型: SUCCESS / WARN / INFO / ERROR
    type            VARCHAR(32)     NOT NULL DEFAULT 'SUCCESS' COMMENT '消息类型',
    -- HTTP 响应状态码: 200、400、401、403、500 等
    response_status INT             NOT NULL DEFAULT 200     COMMENT 'HTTP 状态码',

    PRIMARY KEY (message_key),
    INDEX idx_type (type),
    INDEX idx_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='RSM 统一响应消息模板表';
