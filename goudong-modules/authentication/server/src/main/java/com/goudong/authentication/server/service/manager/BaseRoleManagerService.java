package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.rest.req.search.BaseRoleDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.core.lang.PageResult;

/**
 * 类描述：
 * 角色管理服务层接口
 * @author cfl
 * @version 1.0
 */
public interface BaseRoleManagerService {

    /**
     * 用户所在应用下的角色下拉
     * @param req 条件查询参数
     * @return 角色下拉分页对象
     */
    PageResult<BaseRoleDropDownResp> roleDropDown(BaseRoleDropDownReq req);
}
