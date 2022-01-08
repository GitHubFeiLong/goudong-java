package com.goudong.commons.config;

import com.goudong.commons.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 * 全局异常配置
 * @author msi
 * @version 1.0
 * @date 2021/12/12 19:49
 */
@Configuration
public class GlobalExceptionConfig {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        return new GlobalExceptionHandler(request, response);
    }

}
