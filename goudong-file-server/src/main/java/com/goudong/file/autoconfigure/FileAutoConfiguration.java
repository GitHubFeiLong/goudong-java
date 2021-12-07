package com.goudong.file.autoconfigure;

import com.goudong.commons.constant.SystemEnvConst;
import com.goudong.commons.enumerate.FileTypeEnum;
import com.goudong.file.core.FileType;
import com.goudong.file.core.FileUpload;
import com.goudong.file.properties.FileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
// @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class FileAutoConfiguration {

    private final FileProperties fileProperties;


    public FileAutoConfiguration(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    /**
     * 注入
     * @return
     */
    @Bean
    public FileUpload fileUpload() {
        FileUpload upload = fileProperties.getUpload();
        String rootDir = upload.getRootDir();
        // 获取所有类型
        List<FileType> defaultFileTypes = new ArrayList<>();
        FileTypeEnum.listValues().stream().forEach(p->{
            FileType fileType = new FileType();
            fileType.setType(p);
            defaultFileTypes.add(fileType);
        });

        // 用户配置的类型
        List<FileType> settingFileTypes = new ArrayList<>(upload.getFileTypes());
        // 开启全路径，那么就不需要修改路径。
        if (upload.getEnableFullPathModel()) {
            // // 处理文件类型
        } else {
            boolean startsWith = upload.getRootDir().startsWith("/");
            if (!startsWith) {
                throw new IllegalArgumentException("文件路径配置错误:file.upload.root-dir="+rootDir);
            }

            if (SystemEnvConst.IS_WIN) {//windows
                upload.setRootDir(SystemEnvConst.LOCALAPPDATA+ SystemEnvConst.SEPARATOR + upload);
            } else { //linux
                upload.setRootDir(SystemEnvConst.LOCALAPPDATA+ SystemEnvConst.SEPARATOR + upload);
            }
        }
        Iterator<FileType> iterator = defaultFileTypes.iterator();
        while (iterator.hasNext()) {
            FileType fileType = iterator.next();
            int indexOf = settingFileTypes.indexOf(fileType);
            if (settingFileTypes.contains(fileType)) {
            }
        }

        return upload;
    }
}
