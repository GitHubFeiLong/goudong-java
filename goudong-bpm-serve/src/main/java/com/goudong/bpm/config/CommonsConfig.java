package com.goudong.bpm.config;

import com.goudong.bpm.filter.BpmAuthenticationFilter;
import com.goudong.commons.annotation.enable.*;
import com.goudong.commons.aop.LoggingAop;
import com.goudong.commons.aop.RepeatAop;
import com.goudong.commons.filter.UserContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.Filter;

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
//@EnableCommonsUserContextFilter
//@ServletComponentScan(basePackageClasses = {UserContextFilter.class})
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

    @Bean("userContextFilter")
    public FilterRegistrationBean userContextFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new UserContextFilter());
        filterFilterRegistrationBean.setOrder(0);//执行的顺序，值越低，优先级越高
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }

    /**
     * 请求进入bpm时，将用户信息填入 SecurityContextHolder,并设置 activiti的 AuthenticatedUserId
     * @return
     */
    @Bean("bpmAuthenticationFilter")
    public FilterRegistrationBean bpmAuthenticationFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new BpmAuthenticationFilter());
        filterFilterRegistrationBean.setOrder(10);//执行的顺序，值越低，优先级越高
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }

}