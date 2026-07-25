package com.lingyun.authorization.core.entity;

/**
 * 登录凭证标记接口。
 * <p>
 * 具体项目实现此接口定义自己的登录方式（如 EmailAndPassword、EmailAndCaptcha），
 * 并提供对应的 {@code Authenticator} 来处理认证。
 */
public interface Credential {
}
