package com.goudong.oauth2.service;

import com.goudong.core.lang.PageResult;
import com.goudong.oauth2.dto.BaseAppApplyReq;
import com.goudong.oauth2.dto.BaseAppAuditReq;
import com.goudong.oauth2.dto.BaseAppDTO;
import com.goudong.oauth2.dto.BaseAppQueryReq;

/**
 * 接口描述：
 * 应用接口
 * @author Administrator
 * @version 1.0
 * @date 2023/6/10 22:50
 */
public interface BaseAppService {

    /**
     * 申请应用
     * @param req
     * @return
     */
    BaseAppDTO apply(BaseAppApplyReq req);

    /**
     * 删除应用
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 审核应用
     * @param req
     * @return
     */
    BaseAppDTO audit(BaseAppAuditReq req);

    /**
     * 查询应用
     * @param req
     * @return
     */
    PageResult<BaseAppDTO> query(BaseAppQueryReq req);
    //~methods
    //==================================================================================================================
}
