package com.goudong.user.config;

import com.goudong.commons.annotation.enable.*;
import com.goudong.commons.aop.LoggingAop;
import com.goudong.commons.aop.RedisRandomSecondAop;
import com.goudong.commons.aop.RepeatAop;
import com.goudong.commons.framework.redis.RedisTool;
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
@EnableCommonsWebMvcConfig
@EnableCommonsGlobalExceptionHandler
@EnableCommonsRedisConfig
@EnableCommonsFeignConfig
@EnableCommonsJpaConfig
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
    public LoggingAop loggingAop(Environment environment) {
        return new LoggingAop(environment);
    }

    /**
     * 防止重复请求
     * @return
     */
    // @Bean
    // @ConditionalOnClass(value = {RedisOperationsUtil.class, AuthorityUserUtil.class})
    public RepeatAop repeatAop() {
        return new RepeatAop();
    }

    @Bean
    public RedisRandomSecondAop redisRandomSecondAop(RedisTool redisTool) {
        return new RedisRandomSecondAop(redisTool);
    }

}