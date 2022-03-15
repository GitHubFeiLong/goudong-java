package com.goudong.file.service;

import com.goudong.commons.dto.file.*;
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
     * 预检查文件类型合大小是否符合
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
