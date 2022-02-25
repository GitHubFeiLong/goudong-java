package com.goudong.file.core;

import com.goudong.commons.enumerate.file.FileTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 上传文件的配置
 * @author msi
 * @version 1.0
 * @date 2021/12/4 21:07
 */
@Data
public class FileUpload {

    /**
     * 是否开启文件上传接口
     */
    private Boolean enabled = true;

    /**
     * 开启全路径模式
     */
    private Boolean enableFullPathModel = false;

    /**
     * 上传文件的目录,不论操作系统都使用'/'作为目录分隔符
     * 默认值是 用户目录下的'/goudong-file-server',使用日期作为二级目录.
     */
    private String rootDir = "";

    /**
     * jpg类型
     */
    @NestedConfigurationProperty
    private FileType jpg = new FileType(FileTypeEnum.JPG);
    /**
     * jpeg类型
     */
    @NestedConfigurationProperty
    private FileType jpeg = new FileType(FileTypeEnum.JPEG);
    /**
     * png类型
     */
    @NestedConfigurationProperty
    private FileType png = new FileType(FileTypeEnum.PNG);
    /**
     * gif类型
     */
    @NestedConfigurationProperty
    private FileType gif = new FileType(FileTypeEnum.GIF);
    /**
     * svg类型
     */
    @NestedConfigurationProperty
    private FileType svg = new FileType(FileTypeEnum.SVG);
    /**
     * xls类型
     */
    @NestedConfigurationProperty
    private FileType xls = new FileType(FileTypeEnum.XLS);
    /**
     * xlsx类型
     */
    @NestedConfigurationProperty
    private FileType xlsx = new FileType(FileTypeEnum.XLSX);
    /**
     * doc类型
     */
    @NestedConfigurationProperty
    private FileType doc = new FileType(FileTypeEnum.DOC);
    /**
     * docx类型
     */
    @NestedConfigurationProperty
    private FileType docx = new FileType(FileTypeEnum.DOCX);
    /**
     * ppt类型
     */
    @NestedConfigurationProperty
    private FileType ppt = new FileType(FileTypeEnum.PPT);
    /**
     * pptx类型
     */
    @NestedConfigurationProperty
    private FileType pptx = new FileType(FileTypeEnum.PPTX);
    /**
     * pdf类型
     */
    @NestedConfigurationProperty
    private FileType pdf = new FileType(FileTypeEnum.PDF);
    /**
     * txt类型
     */
    @NestedConfigurationProperty
    private FileType txt = new FileType(FileTypeEnum.TXT);
    /**
     * md类型
     */
    @NestedConfigurationProperty
    private FileType md = new FileType(FileTypeEnum.MD);

    /**
     * 文件类型, 程序将用户配置和默认配置统一设置到属性中，方便后期使用。
     */
    private List<FileType> fileTypes = new ArrayList<>();
}
