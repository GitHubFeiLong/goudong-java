package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.rest.req.BaseAppCreate;
import com.goudong.authentication.server.rest.req.BaseAppUpdate;
import com.goudong.authentication.server.rest.req.search.BaseAppDropDownReq;
import com.goudong.authentication.server.rest.req.search.BaseAppPageReq;
import com.goudong.authentication.server.rest.resp.BaseAppPageResp;
import com.goudong.authentication.server.service.dto.BaseAppDTO;
import com.goudong.core.lang.PageResult;

import java.util.List;

/**
 * Service Interface for managing {@link BaseApp}.
 */
public interface BaseAppService {
    //~methods
    //==================================================================================================================
    /**
     * 根据应用id查询应用
     * @param id 应用id
     * @return 应用对象
     */
    BaseApp findById(Long id);

    /**
     * 应用分页
     * @param req 查询参数
     * @return 分页结果
     */
    PageResult<BaseAppPageResp> page(BaseAppPageReq req);

    /**
     * 新增应用
     * @param req 新增应用参数
     * @return 应用
     */
    BaseAppDTO save(BaseAppCreate req);

    /**
     * 修改应用
     * @param req 修改应用对象
     * @return 修改应用后的对象
     */
    BaseAppDTO update(BaseAppUpdate req);

    /**
     * 删除应用
     * @param id id
     * @return 被删除应用
     */
    BaseAppDTO delete(Long id);

    /**
     * 应用下拉
     * @param req 下拉参数
     * @return 用户能访问的应用下拉列表
     */
    List<BaseAppDropDownReq> dropDown(BaseAppDropDownReq req);

    /**
     * 应用下拉
     * @param req 下拉参数
     * @return 所有应用下拉
     */
    List<BaseAppDropDownReq> allDropDown(BaseAppDropDownReq req);

    /**
     * 根据请求头中{@code X-App-Id}，查询应用
     * @return baseApp
     */
    BaseApp findByHeader();
}
