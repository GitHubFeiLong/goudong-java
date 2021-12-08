package com.goudong.commons.dto.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 类描述：
 * 请求上传文件的对象
 * @author msi
 * @version 1.0
 * @date 2021/12/8 20:38
 */
@Data
public class RequestUploadDTO {
    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 上传文件对象
     */
    @NotNull(message = "上传文件时，文件不能为空")
    private MultipartFile file;
}
