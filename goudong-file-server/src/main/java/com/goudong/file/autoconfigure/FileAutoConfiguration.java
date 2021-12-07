package com.goudong.file.autoconfigure;

import com.goudong.commons.constant.SystemEnvConst;
import com.goudong.commons.enumerate.FileTypeEnum;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.LogUtil;
import com.goudong.file.core.FileType;
import com.goudong.file.core.FileUpload;
import com.goudong.file.properties.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
@Slf4j
@Configuration
@EnableConfigurationProperties(FileProperties.class)
// @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class FileAutoConfiguration {

    private final FileProperties fileProperties;
    private final Environment environment;

    /**
     * 应用名
     * 拼接完整的上传路径， 默认值 goudong-file-server
     */
    private final String applicationName;
    public FileAutoConfiguration(FileProperties fileProperties, Environment environment) {
        this.fileProperties = fileProperties;
        this.environment = environment;
        applicationName = Optional.ofNullable(environment.getProperty("spring.application.name"))
                .orElse("goudong-file-server");
    }

    /**
     * 注入
     * @return
     */
    @Bean
    public FileUpload fileUpload() {
        FileUpload upload = fileProperties.getUpload();
        final String settingRootDir = upload.getRootDir();
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
            // 判断是否正确
            AssertUtil.isDiskPath(rootDir, String.format("配置的路径无效(file.upload.root-dir=%s)", rootDir));
        } else {
            final String forwardSlash = "/";
            boolean startsWith = rootDir.startsWith(forwardSlash);
            if (!startsWith) {
                rootDir = forwardSlash + rootDir;
            }
            rootDir = rootDir.replace(forwardSlash, SystemEnvConst.SEPARATOR);

            rootDir = SystemEnvConst.USER_HOME + SystemEnvConst.SEPARATOR + applicationName + rootDir;

            // 判断是否正确
            AssertUtil.isDiskPath(rootDir, String.format("配置的路径无效(file.upload.root-dir=%s)\n配置后的完整路径是:%s。" +
                    "您可以尝试配置\"file.upload.enable-full-path-model=true\"", settingRootDir, rootDir));
            upload.setRootDir(rootDir);
        }
        Iterator<FileType> iterator = defaultFileTypes.iterator();
        while (iterator.hasNext()) {
            FileType fileType = iterator.next();

            if (settingFileTypes.contains(fileType)) {
                int indexOf = settingFileTypes.indexOf(fileType);
                FileType settingFileType = settingFileTypes.get(indexOf);
                Boolean enabled = settingFileType.getEnabled();
                if (!enabled) {
                    iterator.remove();
                } else {
                    fileType.setLength(settingFileType.getLength());
                    fileType.setFileLengthUnit(settingFileType.getFileLengthUnit());
                }
            }
        }

        upload.setFileTypes(defaultFileTypes);

        StringBuilder body = new StringBuilder();
        body.append("\t[序号]\t[类型]\t\t[上限]\n");
        AtomicInteger integer = new AtomicInteger(1);
        int border = defaultFileTypes.size();
        defaultFileTypes.stream().forEach(p->{
            StringBuilder space = getSpace(p);
            body.append("\t").append(integer.get())
                    .append("\t").append(p.getType())
                    .append(space).append(p.getLength()).append(p.getFileLengthUnit());
            if(integer.getAndIncrement() < border){
                body.append("\n");
            }
        });
        LogUtil.info(log, "\n应用中文件上传的磁盘目录为:'{}',支持上传的文件类型及最大限制如下:\n{}", rootDir,
                body.toString());
        return upload;
    }

    /**
     * 获取相对应的空白字符串，使其表格对齐
     * @param p
     * @return
     */
    private StringBuilder getSpace(FileType p) {
        int length = p.getType().name().length();
        int num = 8 - length;
        StringBuilder spaceSB = new StringBuilder();
        for (int i = 0; i < num; i++) {
            spaceSB.append(" ");
        }
        return spaceSB;
    }

}
