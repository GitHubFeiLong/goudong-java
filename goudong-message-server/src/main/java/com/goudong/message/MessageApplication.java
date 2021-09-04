package com.goudong.message;

import com.goudong.commons.constant.BasePackageConst;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 类描述：
 * , scanBasePackageClasses = {GlobalExceptionHandler.class, LogAOP.class, RedisConfig.class}
 * 注解：
 *  @EnableDiscoveryClient 开启服务注册
 *  @EnableFeignClients 开启feign
 * @Author msi
 * @Date 2021-05-04 22:36
 * @Version 1.0
 */
@EnableFeignClients(basePackages = {BasePackageConst.OPENFEIGN})
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {BasePackageConst.MESSAGE, BasePackageConst.COMMONS})
@MapperScan(basePackages = {BasePackageConst.MESSAGE_MAPPER, BasePackageConst.COMMONS_MAPPER})
@EnableScheduling
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}
