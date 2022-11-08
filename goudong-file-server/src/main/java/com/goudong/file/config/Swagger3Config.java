package com.goudong.file.config;

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
 * 配置 swagger
 * @ClassName Swagger3Config
 * @Author msi
 * @Date 2021/12/4 20:10
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
                .apis(RequestHandlerSelectors.basePackage("com.goudong.file.controller"))
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

    @Bean
    public Docket download() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("下载文件")
                .description("分块下载")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.file.controller.download"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("download")
                .globalRequestParameters(SwaggerUtil.getRequestParameters())
                ;
    }

    @Bean
    public Docket link() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("文件信息")
                .description("上传后的文件预览，文件详细信息")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.file.controller.link"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("link")
                .globalRequestParameters(SwaggerUtil.getRequestParameters())
                ;
    }

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
                .groupName("upload")
                .globalRequestParameters(SwaggerUtil.getRequestParameters())
                ;
    }

    @Bean
    public Docket export() {
        ApiInfo apiInfo =  new ApiInfoBuilder()
                .title("导出")
                .description("导出")
                .version("1.0")
                .contact(new Contact("Evan", "http://www.baidu.com", "123456@qq.com"))
                .build();
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goudong.file.controller.export"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(Arrays.asList("http", "https")))
                .groupName("export")
                .globalRequestParameters(SwaggerUtil.getRequestParameters())
                ;
    }

}
