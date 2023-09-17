package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.rest.resp.BaseMenuGetAllResp;

/**
 * 类描述：
 * 应用管理服务接口
 * @author cfl
 * @version 1.0
 */
public interface BaseMenuManagerService {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 分页查询菜单
     * @param req 分页参数
     * @return 分页结果
     */
    BaseMenuGetAllResp getAll(BaseMenuGetAllReq req);
}
