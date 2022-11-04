package com.goudong.user.config;

import com.goudong.boot.redis.EnableCommonsRedisConfig;
import com.goudong.boot.web.EnableCommonsWebMvcConfig;
import com.goudong.commons.annotation.enable.*;
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
    public LoggingAop loggingAop(Environment environment) {
        return new LoggingAop(environment);
    }

    /**
     * 防止重复请求
     * @return
     */
    // @Bean
    // @ConditionalOnClass(value = {RedisOperationsUtil.class, AuthorityUserUtil.class})
    //public ApiRepeatAop repeatAop() {
    //    return new ApiRepeatAop();
    //}
}
