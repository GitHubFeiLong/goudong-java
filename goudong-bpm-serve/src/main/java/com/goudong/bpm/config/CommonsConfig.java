package com.goudong.bpm.config;

import com.goudong.commons.annotation.enable.*;
import com.goudong.commons.aop.ApiRepeatAop;
import com.goudong.commons.aop.LoggingAop;
import com.goudong.commons.framework.redis.RedisTool;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 引用 commons中的配置
 * @author msi
 * @version 1.0
 * @date 2022/1/23 11:20
 */
@Configuration
@EnableCommonsWebMvcConfig
@EnableCommonsGlobalExceptionHandler
@EnableCommonsRedisConfig
@EnableCommonsFeignConfig
@EnableCommonsJpaConfig
@EnableCommonsJacksonConfig
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
    public LoggingAop loggingAop(Environment environment) {
        return new LoggingAop(environment);
    }

    /**
     * 防止重复请求
     * @return
     */
    @Bean
    public ApiRepeatAop repeatAop(HttpServletRequest request, RedisTool redisTool, RedissonClient redissonClient) {
        return new ApiRepeatAop(request, redisTool, redissonClient);
    }
}