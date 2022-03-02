package com.goudong.commons.dto.file;

import com.goudong.commons.annotation.validator.EnumValidator;
import com.goudong.commons.enumerate.file.FileTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    private long[] successfulShardIndexArray;

    /**
     * 分片上传失败的索引数组
     */
    private long[] unsuccessfulShardIndexArray;
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
                .successfulShardIndexArray(new long[]{})
                .unsuccessfulShardIndexArray(new long[]{})
                .build();
    }

    /**
     * 指定分片上传完成
     * @param percentage 百分比
     * @param successfulShardIndexArray 上传成功索引
     * @param unsuccessfulShardIndexArray 上传失败索引
     * @return
     */
    public static FileShardUploadResultDTO createShardSuccessful(int percentage,
                                                                 long[] successfulShardIndexArray,
                                                                 long[] unsuccessfulShardIndexArray) {
        return FileShardUploadResultDTO.builder()
                .entiretySuccessful(false)
                .shardSuccessful(true)
                .percentage(percentage)
                .successfulShardIndexArray(new long[]{})
                .unsuccessfulShardIndexArray(new long[]{})
                .build();
    }
}