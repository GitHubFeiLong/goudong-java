package com.goudong.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.redis.EnableCommonsRedisConfig;
import com.goudong.boot.web.EnableCommonsWebMvcConfig;
import com.goudong.boot.web.aop.ApiLogAop;
import com.goudong.commons.annotation.enable.*;
import com.goudong.commons.aop.LoggingAop;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
@EnableCommonsWebMvcConfig
@EnableCommonsRedisConfig
@EnableCommonsFeignConfig
@EnableCommonsJpaConfig
@EnableCommonsJacksonConfig
@EnableCommonsUserContextFilter
@EnableCommonsHideMenuConfig
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

    /**
     * 接口日志切面
     * @param environment
     * @return
     */
    @Bean
    public ApiLogAop apiLogAop(Environment environment, ObjectMapper objectMapper) {
        return new ApiLogAop(environment, objectMapper);
    }
}
