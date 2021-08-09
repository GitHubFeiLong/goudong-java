package com.goudong.commons.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

/**
 * @Author msi
 * @Date 2021-05-06 15:20
 * @Version 1.0
 */
//@Configuration
@Deprecated
public class ConfigBean {

    @Bean
    @Primary
    public ResourceProperties resourceProperties() {
        ResourceProperties resourceProperties = new ResourceProperties();
        resourceProperties.setAddMappings(false);
        return resourceProperties;
    }
    @Bean
    @Primary
    public WebMvcProperties webMvcProperties() {
        WebMvcProperties webMvcProperties = new WebMvcProperties();
        webMvcProperties.setThrowExceptionIfNoHandlerFound(true);
        return webMvcProperties;
    }


}
