package com.goudong.exception.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/23 21:47
 */
@Configuration
public class ExceptionConfiguration {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Bean
    public GlobalExceptionHandler GlobalExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        return new GlobalExceptionHandler(request, response);
    }
}
