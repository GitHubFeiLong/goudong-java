package com.goudong.oauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * http://localhost:10003/oauth/authorize?response_type=code&client_id=admin&redirect_uri=http://www.baidu.com&scope=all&state=normal
 * 教程：https://juejin.cn/post/6844903987137740813
 * 访问上面的路径获取code
 * @Author msi
 * @Date 2021-05-25 14:06
 * @Version 1.0
 */
@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.goudong.oauth2.mapper"})
public class Oauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
