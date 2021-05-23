package com.goudong.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

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
                .apis(RequestHandlerSelectors.basePackage("com.goudong.oauth2.controller.open"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("openApi")
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                ;

    }
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContextList = new ArrayList<>();
        List<SecurityReference> securityReferenceList = new ArrayList<>();
        securityReferenceList.add(new SecurityReference("Authorization", scopes()));
        securityContextList.add(SecurityContext
                .builder()
                .securityReferences(securityReferenceList)
                .forPaths(PathSelectors.any())
                .build()
        );
        return securityContextList;
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")};
    }

    /**
     * 这个类决定了你使用哪种认证方式，我这里使用密码模式
     * 其他方式自己摸索一下，完全莫问题啊
     * SecurityScheme 子类 BasicAuth OAuth ApiKey
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> list = new ArrayList<>();

        // Apikey
        list.add(new ApiKey("Authorization", "Authorization", "header"));

        // BasicAuth
        list.add(new BasicAuth("Authorization"));

        // OAuth
        // 验证账号密码登录的接口
//        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("/user/login");
//        OAuth spring_oauth = new OAuthBuilder()
//                .name("spring_oauth")
//                .grantTypes(Collections.singletonList(grantType))
//                .scopes(Arrays.asList(scopes()))
//                .build();
//        list.add(spring_oauth);
        return list;
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
                .apis(RequestHandlerSelectors.basePackage("com.goudong.oauth2.controller.qq"))
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
                .apis(RequestHandlerSelectors.basePackage("com.goudong.oauth2.controller.wechat"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("WeChat")
                ;

    }
}
