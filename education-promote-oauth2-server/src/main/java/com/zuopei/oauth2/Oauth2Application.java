package com.zuopei.oauth2;

import com.zuopei.commons.config.RedisConfig;
import com.zuopei.commons.utils.RedisValueUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @Author msi
 * @Date 2021-04-08 15:23
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = "com.zuopei")
@EnableDiscoveryClient // 启动服务发现
@MapperScan(value = "com.zuopei.oauth2.dao")
public class Oauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
