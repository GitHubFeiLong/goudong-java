package com.goudong.commons.dto.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/12 15:36
 */
@Data
@ApiModel
public class FileDTO {
    /**
     * 原始文件名
     */
    @ApiModelProperty(value = "上传时的文件名")
    private String originalFilename;

    /**
     * 现在文件名
     */
    @ApiModelProperty(value = "新文件名")
    private String currentFilename;

    /**
     * 文件类型
     * @see com.goudong.commons.enumerate.FileTypeEnum
     */
    @ApiModelProperty(value = "文件类型")
    private String fileType;

    /**
     * 文件大小，单位字节
     */
    @ApiModelProperty(value = "文件大小，单位字节")
    private Long size;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小")
    private Long fileLength;

    /**
     * 文件大小单位
     * @see com.goudong.commons.enumerate.FileLengthUnit
     */
    @ApiModelProperty(value = "文件大小单位")
    private String fileLengthUnit;

    /**
     * 文件访问地址
     */
    @ApiModelProperty(value = "文件访问地址")
    private String fileLink;

    /**
     * 文件磁盘地址
     */
    @ApiModelProperty(value = "文件磁盘地址")
    private String filePath;
}
