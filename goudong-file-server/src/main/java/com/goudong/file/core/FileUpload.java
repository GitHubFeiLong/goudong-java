package com.goudong.file.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String rootDir = "/files";

    /**
     * 文件类型
     */
    private Set<FileType> fileTypes = new HashSet<>();
}
