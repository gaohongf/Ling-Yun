package com.lingyun.base.rsm;

import com.lingyun.base.rsm.message.ResponseMessage;
import com.lingyun.base.rsm.message.ResponseMessageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 启动时自动扫描所有 RsmManager 实现，将其 @RsmInfo 消息同步到存储层。
 * <p>
 * 依赖 {@link ResponseMessageService} bean——若未提供（如未引入 lingyun-base-rsm-mybatis），则跳过同步。
 */
public class RsmLoader {


    /**
     * 启动时自动扫描所有 {@link RsmManager} 实现，将 {@link RsmInfo @RsmInfo} 声明的消息同步到数据库。
     * <ul>
     *   <li>新增的消息自动分配递增的 {@code code} 值</li>
     *   <li>已存在的消息（按 messageKey 匹配）更新模板</li>
     *   <li>若未提供 {@link ResponseMessageService}，则跳过同步</li>
     * </ul>
     *
     * @param managers 所有 {@link RsmManager} 实现（Spring 自动注入）
     * @param service  消息存储服务接口（可缺省，缺省时跳过同步）
     */
    @Autowired(required = false)
    public void load(List<RsmManager> managers, ResponseMessageService service) {
        if (service == null) return; // 无消息存储实现，跳过同步
        // 收集所有声明的消息
        List<ResponseMessage> declared = managers.stream()
                .flatMap(m -> m.getResponseMessages().stream())
                .toList();

        service.batchSaveOrUpdate(declared);
    }
}
