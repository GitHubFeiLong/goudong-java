package com.goudong.user.repository;

import com.goudong.user.po.BaseWhitelistPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 类描述：
 * 白名单持久层
 * @author msi
 * @date 2022/1/8 20:58
 * @version 1.0
 */
public interface BaseWhitelistRepository extends JpaRepository<BaseWhitelistPO, Long>, JpaSpecificationExecutor<BaseWhitelistPO> {

}
