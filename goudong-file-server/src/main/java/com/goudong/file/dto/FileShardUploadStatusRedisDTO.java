package com.goudong.file.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 类描述：
 * 文件分片上传的状态，redis中缓存数据
 * @Author e-Feilong.Chen
 * @Date 2022/2/25 14:58
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class FileShardUploadStatusRedisDTO implements Serializable {
    private static final long serialVersionUID = 4838852995122391874L;
    //~fields
    //==================================================================================================================
    /**
     * 当前分片索引(从0开始类似数组索引)
     */
    @ApiModelProperty(value = "当前分片索引")
    private long shardIndex;

    /**
     * 是否分片上传成功
     */
    private Boolean succeed = false;

    /**
     * 临时文件路径
     */
    private String filePath;


    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
}
