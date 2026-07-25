package com.lingyun.base.rsm.message;

/**
 * 响应类型枚举，表示一次请求响应的语义类别。
 * <ul>
 *     <li>{@link #SUCCESS} — 请求成功，预期的业务结果</li>
 *     <li>{@link #WARN} — 请求成功但有需要注意的警告信息</li>
 *     <li>{@link #INFO} — 请求成功，返回提示性信息</li>
 *     <li>{@link #ERROR} — 请求失败，发生错误</li>
 * </ul>
 */
public enum ResponseType {
    /** 成功 — 请求已正确处理，返回预期结果 */
    SUCCESS,
    /** 警告 — 请求已处理，但存在需要注意的情况 */
    WARN,
    /** 信息 — 请求已处理，返回一般性提示信息 */
    INFO,
    /** 错误 — 请求处理失败 */
    ERROR
}
