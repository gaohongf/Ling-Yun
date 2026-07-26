package com.lingyun.base.rsm.jdbc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

import com.lingyun.base.rsm.annotation.EnableRsm;

@Import({
    ResponseMessageRepository.class,
    JdbcResponseMessageService.class
})
@AutoConfiguration
@EnableRsm
public class RsmJdbcPluginAutoConfiguration {
}
