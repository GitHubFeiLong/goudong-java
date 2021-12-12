package com.goudong.file.repository;

import com.goudong.file.po.FilePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 接口描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/11 23:23
 */
@Repository
public interface FileRepository extends JpaRepository<FilePO, Long>, JpaSpecificationExecutor<FilePO> {
}
