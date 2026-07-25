package com.lingyun.base.request;

import java.util.Optional;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 通用请求属性存储基类，抽象了从 Spring {@link RequestAttributes} 中存取指定名称属性的逻辑。
 * <p>
 * 子类通过继承即可获得类型安全的 request-scope 属性存取能力，无需重复编写
 * {@link RequestContextHolder} 的样板代码。
 *
 * @param <T> 存储的属性值类型
 */
public abstract class CustomRequestAttributes<T> {
    protected final String name;

    /**
     * 构造请求属性存取器，指定在 {@link RequestAttributes} 中的属性键名。
     *
     * @param name 属性键名，作为存取时的 key
     */
    protected CustomRequestAttributes(String name) {
        this.name = name;
    }

    /**
     * 直接尝试从上下文获取内容
     */
    @SuppressWarnings("unchecked")
    public T get() {
        RequestAttributes requestAttributes = currentAttributes();
        if (requestAttributes == null)
            return null;
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 以 {@link Optional} 形式获取当前请求属性，若当前没有请求上下文则返回 {@link Optional#empty()}。
     *
     * @return 包装了属性值的 Optional，可能为空
     */
    @SuppressWarnings("unchecked")
    public Optional<T> getOpt() {
        return Optional.of(RequestContextHolder.getRequestAttributes())
                .map(attributes -> (T) attributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST));
    }

    /**
     * 判断当前请求上下文中是否存在该属性。
     *
     * @return true 若属性值不为 null
     */
    public boolean exists() {
        return get() != null;
    }

    /**
     * 设置当前请求属性值，若没有请求上下文则静默忽略。
     *
     * @param value 要设置的属性值
     */
    public void set(T value) {
        RequestAttributes requestAttributes = currentAttributes();
        if (requestAttributes != null) {
            requestAttributes.setAttribute(name, value, RequestAttributes.SCOPE_REQUEST);
        }
    }

    /**
     * 获取当前线程绑定的请求属性，若不在 Web 请求上下文中则抛出异常。
     *
     * @return 当前请求的 {@link RequestAttributes}
     * @throws IllegalStateException 当不在 Spring Web 请求上下文中时
     */
    private static RequestAttributes currentAttributes() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException(
                    "No thread-bound request found — are you calling RSM outside a Spring Web request context?");
        }
        return attrs;
    }
}
