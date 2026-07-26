package com.lingyun.base.rsm.jdbc;

import org.springframework.context.annotation.Import;

@Import({
    ResponseMessageRepository.class,
    JdbcResponseMessageService.class
})
public class RsmJdbcPluginAutoConfiguration {
}
