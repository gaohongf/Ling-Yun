package com.lingyun.base.rsm.mybatis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({
    ResponseMessageMapper.class,
    MybatisResponseMessageService.class
})
@AutoConfiguration
public class RsmMybatisPlusPluginAutoConfiguration {  
}
