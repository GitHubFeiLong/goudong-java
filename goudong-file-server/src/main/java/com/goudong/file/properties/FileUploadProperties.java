package com.goudong.file.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 类描述：
 * 上传文件的配置
 * @author msi
 * @version 1.0
 * @date 2021/12/4 21:07
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.upload", ignoreUnknownFields = true)
public class FileUploadProperties {

    /**
     * 开启全路径模式
     */
    private Boolean enableFullPathModel = false;

    /**
     * 上传文件的目录
     */
    private String rootDir = "files";
}
