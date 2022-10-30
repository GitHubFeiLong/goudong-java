package com.goudong.file.service;

import com.goudong.file.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 接口描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/11 23:20
 */
public interface UploadService {

    /**
     * 预检查文件类型合大小是否符合,并返回上传状态
     * 当文件上传完成过（存在md5相同的已完成上传记录），直接返回上传成功（秒传）
     * 当文件上传一部分，此时比较文件块大小，最后修改时间等有没有变化，如果没变化，就返回上传的二百分比以及成功片和失败片索引的数组
     * 如果变化了，就删除原分片任务，重新初始化分片任务。
     * 当文件未上传，初始化分片任务。
     * @param parameterDTO
     */
    ShardPrefixCheckReturnDTO shardPrefixCheck(ShardPrefixCheckParameterDTO parameterDTO);

    /**
     * 检查文件是否符合配置的文件上传属性
     * @param files
     */
    void checkSimpleUpload(List<MultipartFile> files);

    /**
     * 检查分片上传文件是否符合上传配置
     * @param shardUploadDTO
     */
    void checkShardUpload(FileShardUploadDTO shardUploadDTO);

    /**
     * 简单上传
     * @param requestUploadDTO
     * @return fileDto 上传后的对象
     */
    FileDTO simpleUpload(RequestUploadDTO requestUploadDTO) throws IOException;

    /**
     * 分块上传
     * @param shardUploadDTO
     */
    FileShardUploadResultDTO shardUpload(FileShardUploadDTO shardUploadDTO) throws IOException;
}
