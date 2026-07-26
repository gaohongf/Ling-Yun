package com.lingyun.base.rsm.message;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultResponseMessageServiceImpl implements ResponseMessageService {

    private final Map<String, ResponseMessage> messageMap = new ConcurrentHashMap<>();
    private AtomicInteger codeCount = new AtomicInteger(1000);

    @Override
    public ResponseMessage findByMessageKey(String key) {
        if (key == null) {
            return null;
        }
        return messageMap.get(key);
    }

    @Override
    public List<ResponseMessage> list() {
        return List.copyOf(messageMap.values());
    }

    @Override
    public boolean save(ResponseMessage message) {
        return messageMap.computeIfAbsent(message.getMessageKey(), (k) -> {
            message.setCode(codeCount.getAndAdd(1));
            return message;
        }) == message;
    }

    @Override
    public boolean updateByKey(ResponseMessage message) {
        return messageMap.computeIfPresent(message.getMessageKey(), (key, value) -> {
            value.setResponseStatus(message.getResponseStatus());
            value.setTemplate(message.getTemplate());
            value.setType(message.getType());
            return value;
        }) != null;
    }

    @Override
    public void batchSaveOrUpdate(Collection<ResponseMessage> messages) {
        messages.forEach(msg -> {
            if (!updateByKey(msg)) {
                save(msg);
            }
        });
    }
}
