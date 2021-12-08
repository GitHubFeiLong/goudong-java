package com.goudong.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * 类描述：
 * 配置 swagger
 * @ClassName Swagger3Config
 * @Author msi
 * @Date 2021/12/4 20:10
 * @Version 1.0
 */
@Configuration
public class Swagger3Config {

    @Bean
    public Docket upload() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("上传文件")
                .description("上传文件接口")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.file.controller.upload"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("上传文件接口")
                ;

    }

}
