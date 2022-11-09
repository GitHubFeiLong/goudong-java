package com.goudong.file.dto;

import com.goudong.boot.web.validation.EnumValidator;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 类描述：
 * 文件分片上传对象
 * @author msi
 * @version 1.0
 * @date 2022/2/24 20:04
 */
@Data
@ApiModel
public class FileShardUploadDTO {

    //~fields
    //==================================================================================================================
    /**
     * 文件MD5：实现秒传
     */
    @ApiModelProperty(value = "文件MD5：实现秒传")
    @NotBlank(message = "fileMd5不能为空")
    private String fileMd5;

    /**
     * 原文件名：保留原文件的文件名称
     */
    @ApiModelProperty(value = "原文件名")
    @NotBlank(message = "原文件名不能为空")
    private String fileName;

    /**
     * 文件类型：用来判断是否符合上传规则配置
     */
    @ApiModelProperty(value = "原文件类型")
    @NotNull(message = "文件类型不能为空")
    @EnumValidator(message = "该文件类型不支持", enumClass = FileTypeEnum.class)
    private String fileType;
    /**
     * 文件大小：用来判断是否符合上传规则配置
     */
    @ApiModelProperty(value = "原文件大小")
    @Min(value = 1, message = "文件大小值错误")
    @Max(value = Long.MAX_VALUE, message = "文件大小超出了long的最大值")
    private long fileSize;

    /**
     * 按照指定大小切割
     */
    @ApiModelProperty(value = "分块大小")
    @Min(value = 1, message = "分块大小值不能小于1")
    private long blockSize;

    /**
     * 总分片数量
     */
    @ApiModelProperty(value = "总分片数量")
    @Min(value = 1, message = "总分片数量不能小于1")
    private long shardTotal;

    /**
     * 当前分片索引(从0开始类似数组索引)
     */
    @ApiModelProperty(value = "当前分片索引")
    @Min(value = 0, message = "当前分片索引不能小于0")
    private long shardIndex;

    /**
     * 当前分片的数据
     */
    @ApiModelProperty(value = "当前分片的数据")
    @NotNull(message = "分片数据不能为空")
    private MultipartFile shardData;

    /**
     * 源文件最后修改时间
     */
    @ApiModelProperty(value="源文件最后修改时间")
    @NotNull(message = "源文件最后修改时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifiedTime;

    //~methods
    //==================================================================================================================

}
