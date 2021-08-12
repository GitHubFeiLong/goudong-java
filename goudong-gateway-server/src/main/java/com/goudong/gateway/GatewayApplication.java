package com.goudong.gateway;

import com.goudong.commons.config.RedisConfig;
import com.goudong.commons.constant.BasePackageConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关模块
 * @Author msi
 * @Date 2021-04-08 14:05
 * @Version 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {BasePackageConst.GATEWAY})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
