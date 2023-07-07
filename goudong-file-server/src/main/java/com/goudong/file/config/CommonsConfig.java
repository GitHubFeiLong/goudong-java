package com.goudong.file.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.redis.EnableCommonsRedisConfig;
import com.goudong.boot.web.EnableCommonsWebMvcConfig;
import com.goudong.boot.web.aop.ApiLogAop;
import com.goudong.boot.web.properties.ApiLogProperties;
import com.goudong.commons.annotation.enable.*;
import com.goudong.commons.aop.LoggingAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * commons模块的ben注入
 * TODO ApplicationRunnerConfig
 * @author msi
 * @version 1.0
 * @date 2021/12/11 17:42
 */
@Configuration
@EnableCommonsRedisConfig
@EnableCommonsWebMvcConfig
@EnableCommonsJpaConfig
@EnableCommonsJacksonConfig
@EnableCommonsFeignConfig
@EnableCommonsSpringBeanToolConfig
@EnableCommonsUserContextFilter
public class CommonsConfig {
    private final HttpServletRequest request;

    public CommonsConfig(HttpServletRequest request) {
        this.request = request;
    }
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
    public ApiLogAop apiLogAop(Environment environment, ObjectMapper objectMapper, ApiLogProperties apiLogProperties) {
        return new ApiLogAop(environment, objectMapper, apiLogProperties);
    }

    /**
     * 防止重复请求
     * @return
     */
    // @Bean
    // @ConditionalOnClass(value = {RedisOperationsUtil.class, AuthorityUserUtil.class})
    // public RepeatAop repeatAop() {
    //     return new RepeatAop();
    // }

}
