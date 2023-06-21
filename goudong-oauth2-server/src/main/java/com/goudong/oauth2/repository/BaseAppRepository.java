package com.goudong.oauth2.repository;

import com.goudong.oauth2.po.BaseAppPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

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
    /**
     * 根据应用名查询应用
     * @param appName
     * @return
     */
    Optional<BaseAppPO> findByAppName(String appName);

    /**
     * 根据对外appId查询应用
     * @param appId
     * @return
     */
    Optional<BaseAppPO> findByAppId(String appId);

    /**
     * 根据对外appId查询应用
     * @param appId
     * @param status
     * @return
     */
    Optional<BaseAppPO> findByAppIdAndStatus(String appId, Integer status);
}
