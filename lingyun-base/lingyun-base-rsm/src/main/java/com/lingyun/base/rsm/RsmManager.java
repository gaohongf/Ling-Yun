package com.lingyun.base.rsm;

import com.lingyun.base.rsm.annotation.RsmInfo;
import com.lingyun.base.rsm.message.ResponseMessage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息管理器 — 实现类通过 {@code @RsmInfo} 注解声明消息定义，
 * RsmLoader 在启动时将子类的所有声明的消息同步到数据库。
 */
public interface RsmManager {

    /**
     * 通过反射扫描当前类中标注了 {@link RsmInfo} 的字段，收集所有声明的消息定义。
     * <p>
     * 由 {@link RsmLoader} 在启动时调用，将消息同步到存储层。
     *
     * @return 当前 RsmManager 实现类中所有声明的 {@link ResponseMessage} 列表
     */
    default List<ResponseMessage> getResponseMessages() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<ResponseMessage> messages = new ArrayList<>(declaredFields.length);
        for (Field field : declaredFields) {
            RsmInfo info = field.getAnnotation(RsmInfo.class);
            if (info == null) continue;
            field.setAccessible(true);
            ResponseMessage rm = new ResponseMessage();
            try {
                rm.setMessageKey(field.get(this).toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            rm.setTemplate(info.template());
            rm.setResponseStatus(info.status().value());
            messages.add(rm);
        }
        return messages;
    }
}
