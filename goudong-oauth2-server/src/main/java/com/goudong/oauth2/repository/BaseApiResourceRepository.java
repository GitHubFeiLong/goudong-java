package com.goudong.oauth2.repository;

import com.goudong.oauth2.po.BaseApiResourcePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


/**
 * 类描述：
 * 保存系统中所有api接口资源 持久层
 * @author cfl
 * @date 2022/8/2 22:39
 * @version 1.0
 */
public interface BaseApiResourceRepository extends JpaRepository<BaseApiResourcePO, Long>, JpaSpecificationExecutor<BaseApiResourcePO> {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 根据applicationName查询
     * @param applicationName
     * @return
     */
    List<BaseApiResourcePO> findAllByApplicationName(String applicationName);

}
