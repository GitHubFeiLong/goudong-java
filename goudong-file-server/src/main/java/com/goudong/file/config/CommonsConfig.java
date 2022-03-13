package com.goudong.file.config;

import com.goudong.commons.annotation.enable.*;
import com.goudong.commons.aop.LoggingAop;
import com.goudong.commons.filter.UserContextFilter;
import org.springframework.boot.web.servlet.ServletComponentScan;
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
@EnableCommonsWebMvcConfig
@EnableCommonsGlobalExceptionHandler
@EnableCommonsJpaConfig
@EnableCommonsJacksonConfig
@EnableCommonsFeignConfig
@EnableCommonsRedisConfig
@ServletComponentScan(basePackageClasses = {UserContextFilter.class})
public class CommonsConfig {
    private final HttpServletRequest request;

    public CommonsConfig(HttpServletRequest request) {
        this.request = request;
    }

    /*
    ==========================================================
    ===========================Aop===============================
    ==========================================================
     */
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
    // public RepeatAop repeatAop() {
    //     return new RepeatAop();
    // }

}
