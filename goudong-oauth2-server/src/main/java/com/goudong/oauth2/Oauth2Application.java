package com.goudong.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 类描述：
 *
 * @ClassName Oauth2Service
 * @Author msi
 * @Date 2021/2/9 19:20
 * @Version 1.0
 */
@EnableOpenApi // 开启Swagger
@EnableEurekaClient // 开启eureka客户端
@SpringBootApplication
public class Oauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
