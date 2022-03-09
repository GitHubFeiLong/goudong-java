package com.goudong.commons.dto.file;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 文件分片上传对象返回对象
 * @author msi
 * @version 1.0
 * @date 2022/2/24 20:04
 */
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileShardUploadResultDTO {

    //~fields
    //==================================================================================================================
    /**
     * 整体上传成功
     */
    private Boolean entiretySuccessful;

    /**
     * 分片上传成功
     */
    private Boolean shardSuccessful;

    /**
     * 百分比，必填 0-100
     */
    private int percentage;

    /**
     * 分片上传成功的索引数组
     */
    private List<Long> successfulShardIndexArray;

    /**
     * 分片上传失败的索引数组
     */
    private List<Long> unsuccessfulShardIndexArray;
    //~methods
    //==================================================================================================================

    /**
     * 文件完整上传完成
     * @return
     */
    public static FileShardUploadResultDTO createEntiretySuccessful() {
        return FileShardUploadResultDTO.builder()
                .entiretySuccessful(true)
                .shardSuccessful(true)
                .percentage(100)
                .successfulShardIndexArray(new ArrayList<>())
                .unsuccessfulShardIndexArray(new ArrayList<>())
                .build();
    }

    /**
     * 指定分片上传完成
     * @param successfulShardIndexArray 上传成功索引
     * @param unsuccessfulShardIndexArray 上传失败索引
     * @return
     */
    public static FileShardUploadResultDTO createShardSuccessful(List<Long> successfulShardIndexArray,
                                                                 List<Long> unsuccessfulShardIndexArray) {

        int ss = successfulShardIndexArray.size();
        int uss = unsuccessfulShardIndexArray.size();

        int percentage = ss / (ss + uss) * 100;
        return FileShardUploadResultDTO.builder()
                .entiretySuccessful(false)
                .shardSuccessful(true)
                .percentage(percentage)
                .successfulShardIndexArray(successfulShardIndexArray)
                .unsuccessfulShardIndexArray(unsuccessfulShardIndexArray)
                .build();
    }

}