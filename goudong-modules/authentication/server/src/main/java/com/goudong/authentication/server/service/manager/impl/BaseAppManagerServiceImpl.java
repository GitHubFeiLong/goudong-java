package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.rest.req.BaseAppCreate;
import com.goudong.authentication.server.rest.req.BaseAppPageReq;
import com.goudong.authentication.server.rest.req.BaseAppUpdate;
import com.goudong.authentication.server.rest.req.BaseAppDropDown;
import com.goudong.authentication.server.rest.resp.search.BaseAppPageResp;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.dto.BaseAppDTO;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import com.goudong.core.lang.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 新增应用
     *
     * @param req 新增应用参数
     * @return 新增应用对象
     */
    @Override
    public BaseAppDTO save(BaseAppCreate req) {
        return baseAppService.save(req);
    }

    /**
     * 修改应用
     *
     * @param req 修改应用对象
     * @return 修改应用后的对象
     */
    @Override
    public BaseAppDTO update(BaseAppUpdate req) {
        return baseAppService.update(req);
    }

    /**
     * 根据id删除应用
     *
     * @param id id
     * @return 被删除的应用对象
     */
    @Override
    public BaseAppDTO deleteById(Long id) {
        return baseAppService.delete(id);
    }

    /**
     * 应用下拉，显示当前用户能查询到的应用，超级管理员查询所有，管理员只能查询本应用
     *
     * @param req 查询条件
     * @return 用户能访问的应用列表
     */
    @Override
    public List<BaseAppDropDown> appDropDown(BaseAppDropDown req) {
        return baseAppService.dropDown(req);
    }

    /**
     * 所有应用下拉
     *
     * @param req 查询条件
     * @return 所有应用
     */
    @Override
    public List<BaseAppDropDown> allDropDown(BaseAppDropDown req) {
        return baseAppService.allDropDown(req);
    }

}
