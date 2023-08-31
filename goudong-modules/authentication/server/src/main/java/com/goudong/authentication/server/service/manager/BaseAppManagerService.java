package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.domain.BaseApp;

/**
 * 类描述：
 * 应用管理服务层接口
 * @author chenf
 * @version 1.0
 */
public interface BaseAppManagerService {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 根据{@code appId}查询应用
     * @param appId 应用id
     * @return 应用对象
     */
    BaseApp findById(Long appId);

}
