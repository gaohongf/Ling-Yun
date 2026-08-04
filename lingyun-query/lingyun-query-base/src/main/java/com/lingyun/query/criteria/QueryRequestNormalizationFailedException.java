package com.lingyun.query.criteria;

/**
 * 查询请求标准化失败异常.
 * <p>
 * 当 {@link CriteriaQueryParser#parse(String, Class)} 在解析 JSON 请求时发生
 * {@link com.fasterxml.jackson.core.JsonProcessingException}（如 JSON 格式错误、
 * 字段类型不匹配、嵌套结构解析失败等）时，会抛出本异常以包装原始 Jackson 异常。
 * </p>
 * <p>
 * 本异常继承自 {@link RuntimeException}（非受检），因此不需要在方法签名上显式声明。
 * 建议在 Controller 层通过统一异常切面（如 RSM 的
 * {@code UnifiedFailureResponse}）捕获并返回友好错误响应，
 * 避免让猫猫的 JSON 解析错误直接暴露给前端喵~
 * </p>
 *
 * <h3>典型触发场景</h3>
 * <ul>
 *   <li>请求体不是合法的 JSON 格式</li>
 *   <li>JSON 字段类型与目标实体字段类型不兼容（如把字符串赋给 {@code int} 字段）</li>
 *   <li>嵌套对象反序列化失败</li>
 * </ul>
 *
 * @see CriteriaQueryParser
 * @see CriteriaQuery
 */
public class QueryRequestNormalizationFailedException extends RuntimeException {

    /**
     * 无参构造器（不推荐，建议至少传入错误消息）.
     * <p>
     * 仅在不需要额外上下文信息的极简场景下使用。调试时缺少消息会让猫猫很困惑喵。
     * </p>
     */
    public QueryRequestNormalizationFailedException(){}

    /**
     * 使用指定错误消息构造异常.
     *
     * @param msg 错误描述信息（建议包含失败原因的关键提示）
     */
    public QueryRequestNormalizationFailedException(String msg){
        super(msg);
    }

    /**
     * 使用指定错误消息和原始异常构造异常（推荐使用）.
     * <p>
     * 包装原始异常可以保留完整堆栈，方便排查根因。这是最推荐的构造方式喵~
     * </p>
     *
     * @param msg 错误描述信息
     * @param c   导致标准化失败的原始异常（通常是 Jackson 的 {@code JsonProcessingException}）
     */
    public QueryRequestNormalizationFailedException(String msg ,Throwable c){
        super(msg, c);
    }
}