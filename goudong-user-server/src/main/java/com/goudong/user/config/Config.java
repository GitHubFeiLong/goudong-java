package com.goudong.user.config;

import com.goudong.commons.aop.LoggingAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/10 20:54
 */
@Configuration
public class Config {

    @Resource
    private Environment environment;

    @Bean
    public LoggingAop loggingAop(){
        return new LoggingAop(environment);
    }
}
