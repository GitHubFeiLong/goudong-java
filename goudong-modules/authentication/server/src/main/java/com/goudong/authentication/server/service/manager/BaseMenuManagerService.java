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
     * 查询所有菜单
     * @param req 查询条件
     * @return 树形结构的菜单
     */
    BaseMenuGetAllResp getAll(BaseMenuGetAllReq req);
}
