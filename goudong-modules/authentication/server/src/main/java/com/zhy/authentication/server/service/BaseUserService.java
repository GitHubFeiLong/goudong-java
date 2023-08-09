package com.zhy.authentication.server.service;

import com.goudong.core.lang.PageResult;
import com.zhy.authentication.server.rest.req.BaseUserCreate;
import com.zhy.authentication.server.rest.req.BaseUserUpdate;
import com.zhy.authentication.server.rest.req.search.BaseUserDropDown;
import com.zhy.authentication.server.rest.req.search.BaseUserPage;
import com.zhy.authentication.server.service.dto.BaseUserDTO;
import com.zhy.authentication.server.service.dto.LoginDTO;
import com.zhy.authentication.server.service.dto.MyAuthentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.zhy.authentication.server.domain.BaseUser}.
 */
public interface BaseUserService {

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
    LoginDTO login(MyAuthentication myAuthentication);

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
}
