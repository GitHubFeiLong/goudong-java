package com.goudong.user;

import com.goudong.commons.constant.BasePackageConst;
import com.goudong.user.config.UIProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 类描述：
 * 注解 @EnableConfigurationProperties 开启配置文件映射对象属性（自动转驼峰）
 * @Author msi
 * @Date 2021/2/9 19:20
 * @Vsion 1.0
 */
@EnableConfigurationProperties({UIProperties.class})
@SpringBootApplication(scanBasePackages = {BasePackageConst.USER, BasePackageConst.COMMONS, BasePackageConst.SECURITY})
@MapperScan(basePackages = {BasePackageConst.USER_MAPPER, BasePackageConst.SECURITY_MAPPER})
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients(basePackages = BasePackageConst.OPENFEIGN)
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
