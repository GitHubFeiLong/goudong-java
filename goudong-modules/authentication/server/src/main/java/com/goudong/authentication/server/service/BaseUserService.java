package com.goudong.authentication.server.service;

import com.goudong.authentication.common.core.LoginResp;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseUserCreate;
import com.goudong.authentication.server.rest.req.BaseUserPageReq;
import com.goudong.authentication.server.rest.req.BaseUserUpdate;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDown;
import com.goudong.authentication.server.rest.req.search.BaseUserPageSearchReq;
import com.goudong.authentication.server.rest.resp.BaseUserDropDownResp;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.core.lang.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Interface for managing {@link BaseUser}.
 */
public interface BaseUserService {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 根据应用Id和用户名查询用户
     * @param appId 应用Id
     * @param username 用户名
     * @return 返回用户
     */
    BaseUser findOneByAppIdAndUsername(Long appId, String username);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户对象
     */
    BaseUser findById(Long id);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户对象详细信息（保留角色菜单）
     */
    @Transactional(readOnly = true)
    BaseUser findDetailById(Long id);


    /**
     * 分页获取用户下拉，只返回操作人所在真实应用下的用户
     *
     * @param req 请求参数
     * @return 用户下拉列表
     */
    PageResult<BaseUserDropDownResp> userDropDown(BaseUserDropDown req);

    /**
     * 分页查询用户
     *
     * @param req 分页参数
     * @return 用户分页对象
     */
    PageResult<BaseUserPageSearchReq> page(BaseUserPageReq req);




    //~待删除methods
    //==================================================================================================================
    /**
     * 新增用户
     * @param req
     * @return
     */
    BaseUserDTO save(BaseUserCreate req);

    /**
     * 修改用户
     * @param req
     * @return
     */
    BaseUserDTO save(BaseUserUpdate req);

    /**
     * Get all the baseUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BaseUserDTO> findAll(Pageable pageable);



    /**
     * Get the "id" baseUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BaseUserDTO> findOne(Long id);

    /**
     * Delete the "id" baseUser.
     *
     * @param id the id of the entity.
     */
    Boolean delete(Long id);

    /**
     * 登录信息
     * @param myAuthentication
     * @return
     */
    LoginResp login(MyAuthentication myAuthentication);



    /**
     * 查询用户id详情
     * @param id
     * @return
     */
    @Deprecated
    BaseUserDTO getById(Long id);

}
