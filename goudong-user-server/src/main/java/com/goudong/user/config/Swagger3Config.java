package com.goudong.user.config;

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

    @Bean
    public Docket BaseUserDocket() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("用户角色权限")
                .description("用户角色权限相关接口")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.user.controller.user"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("用户角色权限")
                ;

    }


    /**
     * 开放接口组
     * @return
     */
    @Bean
    public Docket AllApiDocket() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("ALL API")
                .description("开放接口，不需要登录就能访问")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.user.controller"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("ALL API")
                ;
    }

    /**
     * 开放接口组
     * @return
     */
    @Bean
    public Docket openDocket() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("open api")
                .description("开放接口，不需要登录就能访问")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.user.controller.open"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("openApi")
                ;
    }



    @Bean
    public Docket qqDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("QQ模块")
                .description("qq相关的认证")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();

        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                // 全部扫描
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.goudong.user.controller.qq"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                // 在子模块中使用groupName 可以使用右上的下拉功能
                .groupName("QQ")
                ;

    }

    @Bean
    public Docket weChatDocket() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("WeChat")
                .description("微信OAuth")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.user.controller.wechat"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("WeChat")
                ;

    }

    @Bean
    @Deprecated
    public Docket Oauth2Docket() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("用户角色权限title")
                .description("用户角色权限相关接口")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.user.controller.oauth"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("Deprecated 用户角色权限")
                ;

    }

}
