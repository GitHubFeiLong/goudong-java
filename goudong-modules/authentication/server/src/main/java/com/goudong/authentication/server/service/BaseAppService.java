package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.rest.req.BaseAppCreate;
import com.goudong.authentication.server.rest.req.BaseAppPageReq;
import com.goudong.authentication.server.rest.req.BaseAppUpdate;
import com.goudong.authentication.server.rest.req.search.BaseAppDropDown;
import com.goudong.authentication.server.service.dto.BaseAppDTO;
import com.goudong.core.lang.PageResult;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BaseApp}.
 */
public interface BaseAppService {
    //~methods
    //==================================================================================================================

    /**
     * 根据应用id查询应用
     * @param id
     * @return
     */
    BaseApp findById(Long id);

    /**
     * 应用分页
     * @param req
     * @return
     */
    PageResult page(BaseAppPageReq req);




    //~以下待删除methods
    //==================================================================================================================
    /**
     * 新增应用
     * @param req
     * @return
     */
    BaseAppDTO save(BaseAppCreate req);

    /**
     * 修改应用
     * @param req
     * @return
     */
    BaseAppDTO update(BaseAppUpdate req);

    /**
     * 根据请求头的应用id查询应用
     * @return
     */
    Optional<BaseAppDTO> findByHeader();

    /**
     * 根据id查询应用
     * @param id
     * @return
     */
    Optional<BaseAppDTO> findOne(Long id);

    /**
     * 删除应用
     * @param id
     * @return
     */
    void delete(Long id);



    /**
     * 下拉
     * @param req
     * @return
     */
    List<BaseAppDropDown> dropDown(BaseAppDropDown req);

    /**
     * 下拉
     * @param req
     * @return
     */
    List<BaseAppDropDown> allDropDown(BaseAppDropDown req);
}
