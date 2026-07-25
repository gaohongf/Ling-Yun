package com.lingyun.base.rsm.validation;

import com.lingyun.base.rsm.message.ResponseMessage;
import com.lingyun.base.rsm.message.ResponseMessageService;
import com.lingyun.base.rsm.RsmRequestAttribute;
import jakarta.validation.MessageInterpolator;

import java.util.Locale;

/**
 * 数据库驱动的验证消息插值器 — 将 {@code {jakarta.validation.constraints.NotNull.message}}
 * 等约束消息键解析为数据库中存储的模板文本。
 */
public class DatabaseMessageInterpolator implements MessageInterpolator {

    private final MessageInterpolator defaultInterpolator;
    private final ResponseMessageService messageService;

    /**
     * 构造插值器。
     *
     * @param defaultInterpolator 默认的 Jakarta 插值器，用于无法从数据库解析时的回退
     * @param messageService      消息存储服务，用于从数据库查询消息模板
     */
    public DatabaseMessageInterpolator(MessageInterpolator defaultInterpolator, ResponseMessageService messageService) {
        this.defaultInterpolator = defaultInterpolator;
        this.messageService = messageService;
    }

    /**
     * 插值消息模板（使用系统默认 Locale）。
     *
     * @param messageTemplate 消息模板（如 "{jakarta.validation.constraints.NotNull.message}"）
     * @param context         约束上下文
     * @return 解析后的消息文本
     */
    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, Locale.getDefault());
    }

    /**
     * 插值消息模板（指定 Locale）。
     * 优先从数据库查询消息键对应的模板；未找到时回退到默认插值器。
     *
     * @param messageTemplate 消息模板
     * @param context         约束上下文
     * @param locale          目标语言环境
     * @return 解析后的消息文本
     */
    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        // 尝试从数据库解析消息键
        String resolved = resolveMessage(messageTemplate);
        if (resolved != null) {
            return resolved;
        }
        // 回退到默认插值器
        return defaultInterpolator.interpolate(messageTemplate, context, locale);
    }

    /**
     * 从数据库解析消息键对应的模板文本。
     * 仅当 messageTemplate 以 "{...}" 格式包裹时才尝试数据库查询。
     * 解析成功后，将消息实体写入 RsmRequestAttribute 供响应包装器使用。
     *
     * @param messageTemplate 待解析的消息键（如 "{message.key}"）
     * @return 数据库中的模板文本，未找到或格式不匹配时返回 null
     */
    private String resolveMessage(String messageTemplate) {
        if (messageTemplate == null || !messageTemplate.startsWith("{")) return null;
        String key = messageTemplate.substring(1, messageTemplate.length() - 1);
        ResponseMessage msg = messageService.findByMessageKey(key);
        if (msg == null) return null;
        // 通知响应包装器：此消息已由验证框架确认
        RsmRequestAttribute.CONFIRMED_RESPONSE_MESSAGE.set(msg);
        return msg.getTemplate();
    }
}
