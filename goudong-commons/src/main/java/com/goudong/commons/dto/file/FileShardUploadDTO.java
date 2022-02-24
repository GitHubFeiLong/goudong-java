package com.goudong.commons.dto.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 类描述：
 * 文件分片上传对象
 * @author msi
 * @version 1.0
 * @date 2022/2/24 20:04
 */
@Data
public class FileShardUploadDTO {

    //~fields
    //==================================================================================================================
    /**
     * 整个文件的md5，实现秒传
     */
    private String fileMd5;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 总分片数量
     */
    private long shardTotal;

    /**
     * 当前分片索引(从0开始类似数组索引)
     */
    private long shardIndex;

    /**
     * 当前分片的数据
     */
    private MultipartFile shardData;

    //~methods
    //==================================================================================================================

}