package com.goudong.file.autoconfigure;

import com.goudong.file.properties.FileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * 注解的作用如下：
 * Configuration：定义是一个配置类
 * EnableConfigurationProperties：将配置文件的属性绑定到某个类，注入到容器
 * ConditionalOnWebApplication：当满足条件才生效该配置类（这里是当前应用是Web应用）
 * @author msi
 * @version 1.0
 * @date 2021/12/5 12:08
 */
@Configuration
@EnableConfigurationProperties(FileProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class FileAutoConfiguration {

    private final FileProperties fileProperties;

    public FileAutoConfiguration(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }


    // @Bean
    // public FileUploadProperties FileUploadProperties() {
    //     return null;
    // }
}
