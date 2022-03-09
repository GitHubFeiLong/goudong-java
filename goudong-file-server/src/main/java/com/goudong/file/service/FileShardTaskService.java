package com.goudong.file.service;

import com.goudong.commons.dto.file.FileShardUploadDTO;
import com.goudong.file.po.FileShardTaskPO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 接口描述：
 * 文件分片任务服务层接口
 * @Author e-Feilong.Chen
 * @Date 2022/3/8 9:51
 */
public interface FileShardTaskService {

    /**
     * 根据文件的MD5签名，获取分片上传的任务列表
     * @param fileMd5
     * @return
     */
    List<FileShardTaskPO> listByFileMd5(@NotBlank String fileMd5);

    /**
     * 初始化分片任务
     * @param shardUploadDTO 文件分片上传参数
     * @return
     */
    List<FileShardTaskPO> initFileShardTasks(FileShardUploadDTO shardUploadDTO);

    /**
     * 保存分片
     * @param fileShardTaskPO
     * @return
     */
    FileShardTaskPO save(FileShardTaskPO fileShardTaskPO, List<FileShardTaskPO> taskPOS);

    /**
     * 批量删除分片任务
     * @param fileShardTaskPOS
     */
    void deleteAll(List<FileShardTaskPO> fileShardTaskPOS);
}
