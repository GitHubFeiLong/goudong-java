package com.goudong.file.properties;

import com.goudong.file.core.FileImports;
import com.goudong.file.core.FileUpload;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 类描述：
 * 文件服务的自定义配置属性绑定类
 * @author msi
 * @version 1.0
 * @date 2021/12/5 13:22
 */
@Data
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    /**
     * 文件上传的配置
     */
    @NestedConfigurationProperty
    private FileUpload upload;


    /**
     * 使用文件导入数据时的配置
     */
    @NestedConfigurationProperty
    private FileImports imports;
}
