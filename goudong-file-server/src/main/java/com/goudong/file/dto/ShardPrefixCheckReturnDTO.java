package com.goudong.file.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/3/15 20:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShardPrefixCheckReturnDTO implements Serializable {

    //~fields
    //==================================================================================================================
    /**
     * 整体是否上传成功
     */
    @ApiModelProperty("文件是否上传完成")
    private Boolean entiretySuccessful;

    /**
     * 百分比，必填 0-100
     */
    @ApiModelProperty("文件上传百分比")
    private int percentage;

    /**
     * 分片上传成功的索引数组
     */
    @ApiModelProperty("分片上传成功的索引数组")
    private List<Long> successfulShardIndexArray;

    /**
     * 分片上传失败的索引数组
     */
    @ApiModelProperty("分片上传失败的索引数组")
    private List<Long> unsuccessfulShardIndexArray;

    /**
     * 按照指定大小切割
     */
    @ApiModelProperty(value = "按照指定大小切割")
    private long blockSize;

}