package com.goudong.file.repository;

import com.goudong.file.po.FilePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 接口描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/11 23:23
 */
@Repository
public interface FileRepository extends JpaRepository<FilePO, Long>, JpaSpecificationExecutor<FilePO> {
    /**
     * 根据md5查询第一条
     * @param fileMd5
     * @return
     */
    FilePO findFirstByFileMd5(String fileMd5);
}
