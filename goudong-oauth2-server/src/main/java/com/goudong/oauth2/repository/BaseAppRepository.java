package com.goudong.oauth2.repository;

import com.goudong.oauth2.po.BaseAppPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 接口描述：
 * 应用持久层接口
 * @author Administrator
 * @version 1.0
 * @date 2023/6/10 22:51
 */
public interface BaseAppRepository extends JpaRepository<BaseAppPO, Long>, JpaSpecificationExecutor<BaseAppPO> {
    //~methods
    //==================================================================================================================
}
