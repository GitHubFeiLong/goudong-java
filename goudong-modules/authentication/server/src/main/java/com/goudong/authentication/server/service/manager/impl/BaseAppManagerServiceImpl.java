package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.rest.req.BaseAppPageReq;
import com.goudong.authentication.server.rest.resp.search.BaseAppPageResp;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import com.goudong.core.lang.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 类描述：
 *
 * @Author Administrator
 * @Version 1.0
 */
@Service
public class BaseAppManagerServiceImpl implements BaseAppManagerService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppService baseAppService;

    //~methods
    //==================================================================================================================
    /**
     * 根据{@code appId}查询应用
     *
     * @param appId 应用id
     * @return 应用对象
     */
    @Override
    public BaseApp findById(Long appId) {
        return baseAppService.findById(appId);
    }

    /**
     * 分页查询应用
     *
     * @param req 查询条件
     * @return 应用分页
     */
    @Override
    public PageResult<BaseAppPageResp> page(BaseAppPageReq req) {
        return baseAppService.page(req);
    }

}
