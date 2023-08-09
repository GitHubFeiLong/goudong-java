package com.zhy.authentication.server.service;

import com.goudong.core.lang.PageResult;
import com.zhy.authentication.server.rest.req.BaseAppCreate;
import com.zhy.authentication.server.rest.req.BaseAppUpdate;
import com.zhy.authentication.server.rest.req.search.BaseAppDropDown;
import com.zhy.authentication.server.rest.req.search.BaseAppPage;
import com.zhy.authentication.server.service.dto.BaseAppDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.zhy.authentication.server.domain.BaseApp}.
 */
public interface BaseAppService {

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
     * 应用分页
     * @param req
     * @return
     */
    PageResult page(BaseAppPage req);

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
