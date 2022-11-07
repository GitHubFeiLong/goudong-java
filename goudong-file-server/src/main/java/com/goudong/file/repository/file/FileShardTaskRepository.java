package com.goudong.file.repository.file;

import com.goudong.file.po.file.FileShardTaskPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 接口描述：
 * 文件分片任务持久层接口
 * @Author e-Feilong.Chen
 * @Date 2022/3/2 14:48
 */
public interface FileShardTaskRepository extends JpaRepository<FileShardTaskPO, Long>, JpaSpecificationExecutor<FileShardTaskPO> {

    /**
     * 查询所有分片任务
     * @param md5 原文件的md5值
     * @return
     */
    List<FileShardTaskPO> findAllByFileMd5(String md5);
}
