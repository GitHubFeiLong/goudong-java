package com.goudong.message;

import com.goudong.commons.config.RedisConfig;
import com.goudong.commons.exception.GlobalExceptionHandler;
import com.goudong.commons.interceptor.LogAOP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 类描述：
 * , scanBasePackageClasses = {GlobalExceptionHandler.class, LogAOP.class, RedisConfig.class}
 * @Author msi
 * @Date 2021-05-04 22:36
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.goudong.message", "com.goudong.commons"})
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}
