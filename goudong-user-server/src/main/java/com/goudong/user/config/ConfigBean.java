package com.goudong.user.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * 类描述：
 * 配置一些bean
 * @Author msi
 * @Date 2021-04-04 22:01
 * @Version 1.0
 */
@Configuration
public class ConfigBean {

    // TODO 坑真多
    @Bean
    @LoadBalanced // 使用该注解可以使用服务id访问，不然java.net.UnknownHostException: GOUDONG-OAUTH2-SERVER
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
