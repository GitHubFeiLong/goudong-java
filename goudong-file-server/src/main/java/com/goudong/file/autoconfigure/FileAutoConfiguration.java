package com.goudong.file.autoconfigure;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.constant.core.SystemEnvConst;
import com.goudong.commons.exception.core.ApplicationBootFailedException;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.file.core.FileImports;
import com.goudong.file.core.FileType;
import com.goudong.file.core.FileUpload;
import com.goudong.file.properties.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
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
     * 注入 {@code FileUpload}
     * @return
     */
    @Bean
    public FileUpload fileUpload() {
        FileUpload upload = Optional.ofNullable(fileProperties.getUpload()).orElse(new FileUpload());
        final String settingRootDir = upload.getRootDir();
        String rootDir = upload.getRootDir();

        /*
            配置的类型及限制
         */
        List<FileType> fileTypeList = new ArrayList<>();
        Arrays.stream(upload.getClass().getDeclaredFields())
                // 筛选FileType 类型属性
                .filter(f -> Objects.equals(f.getType().getName(), FileType.class.getName()))
                // 只要字段名
                .map(Field::getName)
                // 根据对象和其字段名获取值，并加入到集合
                .forEach(f->{
                    fileTypeList.add(BeanUtil.getProperty(upload, f));
                });

        // 将其设置到属性中，避免后面额外去取值
        upload.setFileTypes(fileTypeList);

        // 开启全路径，那么就不需要修改路径。
        if (upload.getEnableFullPathModel()) {
            // 判断是否正确
            checkPathValid(rootDir);
        } else {
            if (StringUtils.isNotBlank(rootDir) && ! rootDir.startsWith("/")) {
                rootDir = "/" + rootDir;
            }
            rootDir = rootDir.replace("/", SystemEnvConst.SEPARATOR);

            // 默认放在用户目录下
            rootDir = SystemEnvConst.USER_HOME + SystemEnvConst.SEPARATOR + applicationName + rootDir;

            // 判断是否正确
            checkPathValid(rootDir, settingRootDir);
            upload.setRootDir(rootDir);
        }

        StringBuilder body = new StringBuilder();

        final String template = "%-8s%-8s%-8s\n";
        String title = String.format(template, "NUMBER", "TYPE", "MAX");
        body.append(title);
        AtomicInteger number = new AtomicInteger(1);
        fileTypeList.stream().forEach(p->{
            String row = String.format(template, number.getAndIncrement(), p.getType().toString(), String.valueOf(p.getLength()) + p.getFileLengthUnit());
            body.append(row);
        });

        LogUtil.info(log, "\n应用中文件上传的磁盘目录为:\"{}\",支持上传的文件类型及最大限制如下:\n{}", rootDir,
                body.toString());
        return upload;
    }

    /**
     * 注入 {@code FileImports}
     * @return
     */
    @Bean
    public FileImports fileImports() {
        FileImports imports = Optional.ofNullable(fileProperties.getImports()).orElse(new FileImports());
        /*
            配置的类型及限制
         */
        List<FileType> fileTypeList = new ArrayList<>();
        Arrays.stream(imports.getClass().getDeclaredFields())
                // 筛选FileType 类型属性
                .filter(f -> Objects.equals(f.getType().getName(), FileType.class.getName()))
                // 只要字段名
                .map(Field::getName)
                // 根据对象和其字段名获取值，并加入到集合
                .forEach(f->{
                    fileTypeList.add(BeanUtil.getProperty(imports, f));
                });

        // 将其设置到属性中，避免后面额外去取值
        imports.setFileTypes(fileTypeList);

        StringBuilder body = new StringBuilder();

        final String template = "%-8s%-8s%-8s\n";
        String title = String.format(template, "NUMBER", "TYPE", "MAX");
        body.append(title);
        AtomicInteger number = new AtomicInteger(1);
        fileTypeList.stream().forEach(p->{
            String row = String.format(template, number.getAndIncrement(), p.getType().toString(), String.valueOf(p.getLength()) + p.getFileLengthUnit());
            body.append(row);
        });

        LogUtil.info(log, "\n支持导入的文件类型，异步导入的限制如下:\n{}",
                body.toString());
        return imports;
    }

    /**
     * 尝试创建文件夹，检查是否有访问权限
     * @param diskPath 用户自定义的配置的文件存储路径
     */
    private void checkPathValid(String diskPath) {
        // 判断是否正确
        File file = new File(diskPath);
        // 判断是否不存在
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                // 创建失败，证明文件路径错误
                throw new ApplicationBootFailedException("配置文件保存路径无效",
                        String.format("配置的属性file.upload.root-dir=%s 是无效的磁盘路径或者您没有权限访问该路径", diskPath),
                        "file.upload.root-dir配置有权限正常读写的磁盘路径,或使用默认当前项目根目录");
            }
        }
    }

    /**
     * 尝试创建文件夹，检查是否有访问权限
     * @param diskPath 最终拼接后的完整磁盘路径
     * @param settingRootDir 用户自定义的配置的文件存储路径
     */
    private void checkPathValid(String diskPath, String settingRootDir) {
        // 判断是否正确
        File file = new File(diskPath);
        // 判断是否不存在
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                // 创建失败，证明文件路径错误
                throw new ApplicationBootFailedException("配置文件保存路径无效",
                        String.format("配置的属性file.upload.root-dir=%s ，最终值是%s,是无效的磁盘路径或者您没有权限访问该路径",
                                settingRootDir, diskPath),
                        "file.upload.root-dir配置有权限正常读写的磁盘路径");
            }
        }
    }
}
