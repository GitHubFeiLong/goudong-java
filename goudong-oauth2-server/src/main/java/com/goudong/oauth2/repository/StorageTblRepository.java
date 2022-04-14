package com.goudong.oauth2.repository;

import com.goudong.oauth2.po.StorageTblPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 类描述：
 * 白名单持久层
 * @author msi
 * @date 2022/1/15 12:27
 * @version 1.0
 */
public interface StorageTblRepository extends JpaRepository<StorageTblPO, Integer>, JpaSpecificationExecutor<StorageTblPO> {

}
