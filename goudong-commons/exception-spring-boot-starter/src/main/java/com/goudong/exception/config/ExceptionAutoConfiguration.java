package com.goudong.exception.config;

import com.goudong.exception.core.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class ExceptionAutoConfiguration {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Bean
    public GlobalExceptionHandler GlobalExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        return new GlobalExceptionHandler(request, response);
    }
}