package com.goudong.commons.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 类描述：
 * swagger 允许访问
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 9:46
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * mvc 添加静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html");
    }

    /**
     * 不根据url后缀匹配返回指定的数据类型
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
}
