package com.goudong.file.core;

import lombok.Data;

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
     * 开启全路径模式
     */
    private Boolean enableFullPathModel = false;

    /**
     * 上传文件的目录
     */
    private String rootDir = "files";

    /**
     * 文件类型
     */
    private List<FileType> fileTypes;
}
