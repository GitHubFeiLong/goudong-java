package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.rest.req.BaseAppCertCreateReq;
import com.goudong.authentication.server.rest.req.BaseAppCreate;
import com.goudong.authentication.server.rest.req.BaseAppUpdate;
import com.goudong.authentication.server.rest.req.search.BaseAppDropDownReq;
import com.goudong.authentication.server.rest.req.search.BaseAppPageReq;
import com.goudong.authentication.server.rest.resp.BaseAppPageResp;
import com.goudong.authentication.server.service.dto.BaseAppCertDTO;
import com.goudong.authentication.server.service.dto.BaseAppDTO;
import com.goudong.core.lang.PageResult;

import java.util.List;

/**
 * 类描述：
 * 应用管理服务层接口
 * @author chenf
 * @version 1.0
 */
public interface BaseAppManagerService {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 根据{@code appId}查询应用
     * @param appId 应用id
     * @return 应用对象
     */
    BaseApp findById(Long appId);

    /**
     * 分页查询应用
     * @param req 查询条件
     * @return 应用分页
     */
    PageResult<BaseAppPageResp> page(BaseAppPageReq req);

    /**
     * 新增应用
     * @param req 新增应用参数
     * @return 新增应用对象
     */
    BaseAppDTO save(BaseAppCreate req);

    /**
     * 修改应用
     * @param req 修改应用对象
     * @return 修改应用后的对象
     */
    BaseAppDTO update(BaseAppUpdate req);

    /**
     * 根据id删除应用
     * @param id id
     * @return 被删除的应用对象
     */
    BaseAppDTO deleteById(Long id);

    /**
     * 应用下拉，显示当前用户能查询到的应用，超级管理员查询所有，管理员只能查询本应用
     * @param req 查询条件
     * @return 用户能访问的应用列表
     */
    List<BaseAppDropDownReq> appDropDown(BaseAppDropDownReq req);

    /**
     * 所有应用下拉
     * @param req 查询条件
     * @return 所有应用
     */
    List<BaseAppDropDownReq> allDropDown(BaseAppDropDownReq req);

    /**
     * 根据请求头中{@code X-App-Id}，查询应用
     * @return baseApp
     */
    BaseApp findByHeader();

    /**
     * 查询应用的所有证书
     * @param appId 应用id
     * @return 应用所有证书
     */
    List<BaseAppCertDTO> listCertsByAppId(Long appId);

    /**
     * 创建证书
     * @param req 创建参数
     * @return 证书记录
     */
    BaseAppCertDTO createCert(BaseAppCertCreateReq req);
}
