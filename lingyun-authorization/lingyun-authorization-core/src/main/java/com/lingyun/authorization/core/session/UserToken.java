package com.lingyun.authorization.core.session;

import java.io.Serializable;

/**
 * Token 载体 — 记录用户 ID 和序列号，用于 JWT payload 或分布式缓存 key。
 *
 * @param id     用户 ID
 * @param serial 序列号（UUID），用于区分同一用户的不同会话
 */
public record UserToken(Serializable id, String serial) {
}
