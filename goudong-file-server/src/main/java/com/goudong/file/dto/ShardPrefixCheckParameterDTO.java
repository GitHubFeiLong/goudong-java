package com.goudong.file.dto;

import com.goudong.commons.annotation.validator.EnumValidator;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：
 * 分片上传进行预检的参数对象
 * @author msi
 * @version 1.0
 * @date 2022/3/15 19:45
 */
@Data
public class ShardPrefixCheckParameterDTO implements Serializable {
    private static final long serialVersionUID = -308501676536642409L;

    //~fields
    //==================================================================================================================
    /**
     * 文件MD5：实现秒传
     */
    @ApiModelProperty(value = "文件MD5：实现秒传")
    @NotBlank(message = "fileMd5不能为空")
    private String fileMd5;

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
     * 源文件最后修改时间
     */
    @ApiModelProperty(value="源文件最后修改时间")
    @NotNull(message = "源文件最后修改时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifiedTime;

    //~methods
    //==================================================================================================================

}