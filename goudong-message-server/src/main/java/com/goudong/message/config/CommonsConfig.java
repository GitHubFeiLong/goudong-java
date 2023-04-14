package com.goudong.message.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.redis.EnableCommonsRedisConfig;
import com.goudong.boot.web.EnableCommonsWebMvcConfig;
import com.goudong.commons.annotation.enable.EnableCommonsFeignConfig;
import com.goudong.commons.annotation.enable.EnableCommonsJacksonConfig;
import com.goudong.commons.annotation.enable.EnableCommonsUserContextFilter;
import com.goudong.commons.aop.LoggingAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 类描述：
 * 引用 commons中的配置
 * @author msi
 * @version 1.0
 * @date 2022/1/23 11:20
 */
@Configuration
@EnableCommonsRedisConfig
@EnableCommonsWebMvcConfig
@EnableCommonsFeignConfig
@EnableCommonsJacksonConfig
@EnableCommonsUserContextFilter
public class CommonsConfig {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 日志切面
     * @param environment
     * @return
     */
    @Bean
    public LoggingAop loggingAop(Environment environment, ObjectMapper objectMapper) {
        return new LoggingAop(environment, objectMapper);
    }

}
