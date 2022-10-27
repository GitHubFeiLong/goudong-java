package com.goudong.file.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class RequestUploadDTO {
    /**
     * 自定义文件名，不加后缀
     */
    @ApiModelProperty("自定义文件名,不需要加文件后缀")
    private String originalFilename;

    /**
     * 上传文件对象
     */
    @NotNull(message = "上传文件时，文件不能为空")
    @ApiModelProperty(value = "文件对象", required = true)
    private MultipartFile file;
}
