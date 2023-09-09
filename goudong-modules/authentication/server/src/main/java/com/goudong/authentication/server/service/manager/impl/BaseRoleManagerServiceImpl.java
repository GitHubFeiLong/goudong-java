package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.server.rest.req.search.BaseRoleDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.manager.BaseRoleManagerService;
import com.goudong.core.lang.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 类描述：
 * 角色管理服务层接口实现类
 * @author cfl
 * @version 1.0
 */
@Service
public class BaseRoleManagerServiceImpl implements BaseRoleManagerService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseRoleService baseRoleService;

    //~methods
    //==================================================================================================================
    /**
     * 用户所在应用下的角色下拉
     *
     * @param req 条件查询参数
     * @return 角色下拉分页对象
     */
    @Override
    public PageResult<BaseRoleDropDownResp> roleDropDown(BaseRoleDropDownReq req) {
        return baseRoleService.roleDropDown(req);
    }

}
