package com.goudong.file.service.impl;

import cn.hutool.core.io.FileUtil;
import com.goudong.commons.dto.file.FileShardUploadDTO;
import com.goudong.commons.framework.redis.RedisTool;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.file.enumerate.RedisKeyProviderEnum;
import com.goudong.file.po.FileShardTaskPO;
import com.goudong.file.repository.FileShardTaskRepository;
import com.goudong.file.service.FileShardTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 文件分片任务服务层接口实现类
 * @Author e-Feilong.Chen
 * @Date 2022/3/8 9:52
 */
@Slf4j
@Service
public class FileShardTaskServiceImpl implements FileShardTaskService {
    //~fields
    //==================================================================================================================

    /**
     * 文件分片任务持久层接口
     */
    private final FileShardTaskRepository fileShardTaskRepository;

    /**
     * 自定义redis操作对象
     */
    private final RedisTool redisTool;
    //~construct methods
    //==================================================================================================================

    public FileShardTaskServiceImpl(FileShardTaskRepository fileShardTaskRepository, RedisTool redisTool) {
        this.fileShardTaskRepository = fileShardTaskRepository;
        this.redisTool = redisTool;
    }
    //~methods
    //==================================================================================================================

    /**
     * 根据文件的MD5签名，获取分片上传的任务列表
     *
     * @param fileMd5
     * @return
     */
    @Override
    public List<FileShardTaskPO> listByFileMd5(String fileMd5) {
        // 先从Redis中取
        List<FileShardTaskPO> fileShardTaskPOS =
                redisTool.getList(RedisKeyProviderEnum.FILE_SHARD_UPLOAD_TASK, FileShardTaskPO.class, fileMd5);
        if (CollectionUtils.isNotEmpty(fileShardTaskPOS)) {
            return fileShardTaskPOS;
        }
        // redis没有：查询分片上传任务
        List<FileShardTaskPO> taskPOS = fileShardTaskRepository.findAllByFileMd5(fileMd5);

        // 存在值，就存进Redis中。
        if (CollectionUtils.isNotEmpty(taskPOS)) {
            redisTool.set(RedisKeyProviderEnum.FILE_SHARD_UPLOAD_TASK, taskPOS, fileMd5);
        }

        return taskPOS;
    }

    /**
     * 初始化分片任务
     * 事务传播行为：以非事务的方式执行操作，当前存在事务就挂起事务
     * @param shardUploadDTO 文件分片上传参数
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public List<FileShardTaskPO> initFileShardTasks(FileShardUploadDTO shardUploadDTO) {
        LogUtil.debug(log, "初始化分片任务，开始创建分片任务列表");
        List<FileShardTaskPO> taskPOS = new ArrayList<>();
        String fileMd5 = shardUploadDTO.getFileMd5();
        // 第一次上传
        for (long i = 0, total = shardUploadDTO.getShardTotal(); i < total; i++) {
            taskPOS.add(
                    FileShardTaskPO.builder()
                            .fileMd5(fileMd5)
                            .blockSize(shardUploadDTO.getBlockSize())
                            .successful(false)
                            .tempPath("")
                            .shardIndex(i)
                            .lastModifiedTime(shardUploadDTO.getLastModifiedTime())
                            .build()
            );
        }
        // 保存到数据库
        fileShardTaskRepository.saveAll(taskPOS);

        // 保存到Redis中
        if (CollectionUtils.isNotEmpty(taskPOS)) {
            LogUtil.debug(log, "初始分片任务（md5:{}）到redis", fileMd5);
            redisTool.set(RedisKeyProviderEnum.FILE_SHARD_UPLOAD_TASK, taskPOS, fileMd5);
            LogUtil.debug(log, "初始分片任务（md5：{}）到redis成功", fileMd5);
        }

        LogUtil.debug(log, "初始化分片任务已完成");
        return taskPOS;
    }

    /**
     * 保存分片
     *
     * @param fileShardTaskPO
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public FileShardTaskPO save(FileShardTaskPO fileShardTaskPO, List<FileShardTaskPO> taskPOS) {
        // 保存到redis中
        String key = redisTool.getKey(RedisKeyProviderEnum.FILE_SHARD_UPLOAD_TASK, fileShardTaskPO.getFileMd5());
        int index = taskPOS.indexOf(fileShardTaskPO);
        redisTool.opsForList().set(key, index, fileShardTaskPO);

        // 数据库跟着改
        this.fileShardTaskRepository.save(fileShardTaskPO);

        return fileShardTaskPO;
    }

    /**
     * 批量删除分片任务
     *
     * @param fileShardTaskPOS
     */
    @Override
    @Transactional
    public void deleteAll(List<FileShardTaskPO> fileShardTaskPOS) {
        LogUtil.debug(log, "开始删除分片任务及其临时文件");
        // 将任务进行删除(物理删除)
        fileShardTaskRepository.deleteInBatch(fileShardTaskPOS);
        // 删除redis中的数据
        redisTool.deleteKey(RedisKeyProviderEnum.FILE_SHARD_UPLOAD_TASK, fileShardTaskPOS.get(0).getFileMd5());
        LogUtil.debug(log,"删除分片上传文件任务成功");
        // 删除临时文件
        String tempPath = fileShardTaskPOS.get(0).getTempPath();
        // hutool递归删除
        FileUtil.del(new File(tempPath).getParentFile());
        LogUtil.debug(log,"删除临时文件成功");
    }

}
