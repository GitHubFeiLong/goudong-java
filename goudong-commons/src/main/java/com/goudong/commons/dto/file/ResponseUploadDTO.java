package com.goudong.commons.dto.file;

import com.goudong.commons.enumerate.FileLengthUnit;
import lombok.Data;

/**
 * 类描述：
 * 上传文件后响应对象
 * @author msi
 * @version 1.0
 * @date 2021/12/8 20:38
 */
@Data
public class ResponseUploadDTO {
    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 现在文件名
     */
    private String currentFileName;

    /**
     * 文件大小，单位字节
     */
    private Long size;

    /**
     * 文件大小
     */
    private Long fileLength;

    /**
     * 文件大小单位
     */
    private FileLengthUnit fileLengthUnit;

    /**
     * 文件访问地址
     */
    private String accessUrl;

}
