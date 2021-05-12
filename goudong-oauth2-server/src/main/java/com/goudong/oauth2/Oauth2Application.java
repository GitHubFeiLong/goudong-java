package com.goudong.oauth2;

import com.goudong.commons.constant.BasePackageConst;
import com.goudong.oauth2.config.QQApplicationValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 类描述：
 * 注解 @EnableOpenApi 开启Swagger
 * 注解 @EnableConfigurationProperties 开启配置文件映射对象属性（自动转驼峰）
 * @ClassName Oauth2Service
 * @Author msi
 * @Date 2021/2/9 19:20
 * @Version 1.0
 */
@EnableOpenApi
@EnableConfigurationProperties({QQApplicationValue.class})
@SpringBootApplication(scanBasePackages = {BasePackageConst.OAUTH2, BasePackageConst.COMMONS, BasePackageConst.SECURITY})
@MapperScan("com.goudong.oauth2.dao")
public class Oauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
