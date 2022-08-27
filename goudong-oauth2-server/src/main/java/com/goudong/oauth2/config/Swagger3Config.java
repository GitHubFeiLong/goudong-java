package com.goudong.oauth2.config;

import com.goudong.commons.utils.core.SwaggerUtil;
import io.swagger.annotations.ApiOperation;
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
 * 配置 swagger<br>
 * 访问 ip:port/swagger-ui/index.html
 * ip:port/swagger-ui
 *
 * @ClassName Swagger3Config
 * @Author msi
 * @Date 2020/10/17 10:00
 * @Version 1.0
 */
@Configuration
public class Swagger3Config {

    /**
     * 默认全部扫描
     * @return
     */
    @Bean
    public Docket defaultDocket() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("all api")
                .description("全部接口归纳")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.oauth2.controller"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("all api")
                .globalRequestParameters(SwaggerUtil.getRequestParameters())
                ;
    }

    /**
     * 认证相关
     * @return
     */
    @Bean
    public Docket authenticationDocket() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("认证/鉴权")
                .description("认证/鉴权相关接口")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.oauth2.controller.authentication"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("登录")
                // 添加全局请求参数
                .globalRequestParameters(SwaggerUtil.getRequestParameters())
                ;
    }

}
