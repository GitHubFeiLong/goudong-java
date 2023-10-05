package com.goudong.authentication.server.config;

import com.goudong.authentication.server.constant.HttpHeaderConst;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

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
                .apis(RequestHandlerSelectors.basePackage("com.goudong.authentication.server.rest"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("all api")
                .globalRequestParameters(getRequestParameters())
                ;
    }

    /**
     * 接口添加统一参数
     * @return
     */
    public static List<RequestParameter> getRequestParameters() {
        RequestParameterBuilder requestParameterBuilder = new RequestParameterBuilder();
        List<RequestParameter> requestParameters = new ArrayList<>();
        // token
        requestParameters.add(new RequestParameterBuilder()
                .name("Authorization")
                .description("用户令牌")
                .required(false)
                .in(ParameterType.HEADER)
                .build());

        // 应用Id
        requestParameters.add(new RequestParameterBuilder()
                .name(HttpHeaderConst.X_APP_ID)
                .description("应用ID")
                .required(true)
                .in(ParameterType.HEADER)
                .build());

        // RSA公钥将AES密钥加密后的秘串
        requestParameters.add(new RequestParameterBuilder()
                .name(HttpHeaderConst.X_AES_KEY)
                .description("RSA公钥将AES密钥加密后的秘串")
                .required(false)
                .in(ParameterType.HEADER)
                .build());

        // 接口日志打印返回值的长度限制
        requestParameters.add(new RequestParameterBuilder()
                .name(HttpHeaderConst.X_API_RESULT_LENGTH)
                .description("接口日志打印返回值的长度限制")
                .required(false)
                .in(ParameterType.HEADER)
                .build());

        return requestParameters;
    }
}
