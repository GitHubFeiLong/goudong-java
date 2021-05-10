package com.goudong.message;

import com.goudong.commons.constant.BasePackageConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 类描述：
 * , scanBasePackageClasses = {GlobalExceptionHandler.class, LogAOP.class, RedisConfig.class}
 * 注解 @EnableFeignClients 开启feign
 * @Author msi
 * @Date 2021-05-04 22:36
 * @Version 1.0
 */
@EnableFeignClients(basePackages = {BasePackageConst.COMMONS})
@SpringBootApplication(scanBasePackages = {BasePackageConst.MESSAGE, BasePackageConst.COMMONS})
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}
