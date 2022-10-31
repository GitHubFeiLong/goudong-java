package com.goudong.boot.web.config;

import com.goudong.boot.web.core.GlobalExceptionHandler;
import com.goudong.boot.web.core.WebProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 * 自动配置全局异常当{@code commons.goudong.exception.enable=true}时开启
 * @author cfl
 * @version 1.0
 * @date 2022/10/23 21:47
 */
@Configuration
@EnableConfigurationProperties(WebProperties.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "commons.goudong.web", name = "enable", havingValue = "true", matchIfMissing = true)
public class WebAutoConfiguration {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Bean
    public GlobalExceptionHandler GlobalExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        return new GlobalExceptionHandler(request, response);
    }
}
