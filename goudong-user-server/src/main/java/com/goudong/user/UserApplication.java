package com.goudong.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 类描述：
 * 用户模块启动类
 * @ClassName UserService
 * @Author msi
 * @Date 2021/2/9 16:56
 * @Version 1.0
 */
@EnableOpenApi // 开启OpenApi(Swagger)
@EnableEurekaClient // 开启Eureka客户端
@EnableDiscoveryClient // 启动服务发现
@EnableTransactionManagement // 开启事务管理
@SpringBootApplication()
@MapperScan(basePackages = {"com.goudong.user.dao"}) // 可以不在mapper层添加注解
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
