package com.lingyun.authorization.ra.mvc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@AutoConfiguration
public class MvcRaAutoConfiguration {

    @Bean
    public BranchResourceInfoBuilder branchResourceInfoBuilder(){
        return new BranchResourceInfoBuilder();
    }


    @Bean
    public WebMvcRegistrations webMvcRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new BranchRequestMappingHandlerMapping();
            }
        };
    }
}
