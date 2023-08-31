package com.goudong.authentication.server.service;

import com.goudong.authentication.common.core.LoginResp;
import com.goudong.authentication.common.core.UserDetail;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseUserCreate;
import com.goudong.authentication.server.rest.req.BaseUserUpdate;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDown;
import com.goudong.authentication.server.rest.req.search.BaseUserPage;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.core.lang.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
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
     * 分页查询
     * @param req
     * @return
     */
    PageResult page(BaseUserPage req);

    /**
     * 查询用户id详情
     * @param id
     * @return
     */
    @Deprecated
    BaseUserDTO getById(Long id);

    /**
     * 用户下拉选
     * @param req
     * @return
     */
    List<BaseUserDropDown> dropDown(BaseUserDropDown req);

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    UserDetail getUserDetailByToken(String token);
}
