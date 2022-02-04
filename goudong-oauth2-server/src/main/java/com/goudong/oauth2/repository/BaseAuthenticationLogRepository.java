package com.goudong.oauth2.repository;

import com.goudong.oauth2.po.BaseAuthenticationLogPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 接口描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/2/4 14:16
 */
public interface BaseAuthenticationLogRepository extends JpaRepository<BaseAuthenticationLogPO, Long>, JpaSpecificationExecutor<BaseAuthenticationLogPO> {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

}
