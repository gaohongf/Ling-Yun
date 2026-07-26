package com.lingyun.base.rsm.mybatis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

import com.lingyun.base.rsm.annotation.EnableRsm;

@Import({
    ResponseMessageMapper.class,
    MybatisResponseMessageService.class
})
@AutoConfiguration
@EnableRsm
public class RsmMybatisPlusPluginAutoConfiguration {  
}
