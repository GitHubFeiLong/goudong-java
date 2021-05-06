package com.goudong.oauth2;

import com.goudong.oauth2.config.QQApplicationValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 类描述：
 * EnableOpenApi 开启Swagger
 * EnableConfigurationProperties
 * @ClassName Oauth2Service
 * @Author msi
 * @Date 2021/2/9 19:20
 * @Version 1.0
 */
@EnableOpenApi
@EnableConfigurationProperties({QQApplicationValue.class})
@SpringBootApplication(scanBasePackages = {"com.goudong.oauth2", "com.goudong.commons"})
@MapperScan("com.goudong.oauth2.dao")
public class Oauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
