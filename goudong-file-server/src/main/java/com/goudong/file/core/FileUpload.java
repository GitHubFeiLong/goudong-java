package com.goudong.file.core;

import lombok.Data;

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
     * 上传文件的目录,不论操作系统都使用'/'作为目录分隔符 默认值是 '/goudong-file-server'.
     */
    private String rootDir = "";

    /**
     * 文件类型
     */
    private List<FileType> fileTypes = new ArrayList<>();
}
