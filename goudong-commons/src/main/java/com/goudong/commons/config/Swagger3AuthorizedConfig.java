package com.goudong.commons.config;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 配置 swagger<br>
 * 访问 ip:port/swagger-ui/index.html#/
 * ip:port/swagger-ui
 * @ClassName Swagger3Config
 * @Author msi
 * @Date 2020/10/17 10:00
 * @Version 1.0
 */
@Deprecated
public class Swagger3AuthorizedConfig {

    /**
     * 添加作用域
     * @return
     */
    public static List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContextList = new ArrayList<>();
        List<SecurityReference> securityReferenceList = new ArrayList<>();
        // 将认证方式添加进上下文
        securityReferenceList.add(new SecurityReference("token", scopes()));
        securityReferenceList.add(new SecurityReference("登录", scopes()));

        securityContextList.add(SecurityContext
                .builder()
                .securityReferences(securityReferenceList)
                .forPaths(PathSelectors.any())
                .build()
        );

        return securityContextList;
    }

    /**
     * 范围全局
     * @return
     */
    private static AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")};
    }

    /**
     * 添加认证方式
     * @return
     */
    public static List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> list = new ArrayList<>();

        // 自动加上"Bearer "。
        HttpAuthenticationScheme jwt = HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("token")
                .description("使用token字符串，注意：不需要添加Bearer ")
                .build();

        // Authorization请求头，值是类似 "Basic YWRtaW46MTIzNDU2" 格式，启动"Basic " 是固定的，后面是Base64格式，解码后是键值对
        HttpAuthenticationScheme basic = HttpAuthenticationScheme.BASIC_AUTH_BUILDER.name("登录")
                .description("使用用户名密码登录")
                .build();

        list.add(jwt);
        list.add(basic);

        return list;
    }
}
