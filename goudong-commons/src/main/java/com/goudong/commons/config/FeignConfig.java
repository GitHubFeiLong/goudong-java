package com.goudong.commons.config;

import com.goudong.commons.constant.BasePackageConst;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * 类描述：
 * Feigin配置
 * @Author msi
 * @Date 2021-08-21 9:12
 * @Version 1.0
 */
@Configuration
@EnableFeignClients(basePackages = {BasePackageConst.OPENFEIGN})
public class FeignConfig {
    /**
     * openfeign 需要HTTP MessageConverters
     * @param converters
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    /**
     * 日志级别，生产需要调到info及以上
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }


    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //1.RequestContextHolder拿到当前请求的数据，相当与拿到controller入参的HttpServletRequest
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null) {
                    //老请求
                    HttpServletRequest request = requestAttributes.getRequest();

                    //2.同步请求头信息->cookie
                    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
                    requestTemplate.header(HttpHeaders.AUTHORIZATION, token);
                }

            }
        };
    }
}
