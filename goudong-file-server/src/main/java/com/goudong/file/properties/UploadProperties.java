package com.goudong.file.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * 类描述：
 * 上传文件的配置
 * @author msi
 * @version 1.0
 * @date 2021/12/4 21:07
 */
@Data
@ConfigurationProperties(prefix = "file.upload", ignoreUnknownFields = true)
public class UploadProperties {

    private String rootDir;
    private Set<String> fileTypes;
}
