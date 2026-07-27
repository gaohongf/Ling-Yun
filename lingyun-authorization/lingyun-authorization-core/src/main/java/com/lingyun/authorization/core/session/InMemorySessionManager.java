package com.lingyun.authorization.core.session;

import com.lingyun.authorization.core.entity.User;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link SessionManager} 的内存默认实现——无需外部存储，开箱即用。
 * <p>
 * Token 为随机 UUID（去横线），用户-Token 映射存储在 {@link ConcurrentHashMap} 中，
 * 应用重启后所有会话自动失效。适用于开发调试或无持久化需求的场景。
 * <p>
 * 消费方可通过实现自定义 {@link SessionManager}（如 JWT + Redis）来替换。
 */
public class InMemorySessionManager implements SessionManager {

    private final Map<String, User> tokenStore = new ConcurrentHashMap<>();
    private final Map<Serializable, String> userTokenIndex = new ConcurrentHashMap<>();

    @Override
    public User parse(String token) {
        if (token == null) return null;
        return tokenStore.get(token);
    }

    @Override
    public String issue(User user) {
        // 先移除旧 Token，保证一个用户只有一个有效会话
        String oldToken = userTokenIndex.get(user.getId());
        if (oldToken != null) {
            tokenStore.remove(oldToken);
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenStore.put(token, user);
        userTokenIndex.put(user.getId(), token);
        return token;
    }

    @Override
    public void remove(User user) {
        String token = userTokenIndex.remove(user.getId());
        if (token != null) {
            tokenStore.remove(token);
        }
    }
}
