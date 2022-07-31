package com.goudong.commons.config;

import com.goudong.commons.filter.UserContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 类描述：
 * UserContextFilter过滤器配置到过滤器链中，并设置顺序为0
 * @author cfl
 * @version 1.0
 * @date 2022/8/1 0:38
 */
@Configuration
public class UserContextFilterConfig {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 添加过滤器到过滤器链中，并设置优先级。
     * @return
     */
    @Bean
    public FilterRegistrationBean userContextFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new UserContextFilter());
        // 执行的顺序(值越低，优先级越高)
        filterFilterRegistrationBean.setOrder(0);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }
}
