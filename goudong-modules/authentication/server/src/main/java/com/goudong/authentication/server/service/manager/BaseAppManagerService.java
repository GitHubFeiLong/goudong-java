package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.rest.req.BaseAppPageReq;
import com.goudong.core.lang.PageResult;

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

    /**
     * 分页查询应用
     * @param req 查询条件
     * @return 应用分页
     */
    PageResult<BaseAppPageReq> page(BaseAppPageReq req);
}
