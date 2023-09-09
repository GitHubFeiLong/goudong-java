package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.rest.req.BaseRoleCreate;
import com.goudong.authentication.server.rest.req.BaseRoleUpdate;
import com.goudong.authentication.server.rest.req.search.BaseRoleDropDownReq;
import com.goudong.authentication.server.rest.req.search.BaseRolePage;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import com.goudong.core.lang.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BaseRole}.
 */
public interface BaseRoleService {

    /**
     * 用户所在应用下的角色下拉
     *
     * @param req 条件查询参数
     * @return 角色下拉分页对象
     */
    PageResult<BaseRoleDropDownResp> roleDropDown(BaseRoleDropDownReq req);


    /**
     * 根据角色id集合查询角色
     * @param ids 需要查询的角色id集合
     * @return 角色集合
     */
    List<BaseRole> listByIds(List<Long> ids);


    // 待删除

    /**
     * 新增角色
     * @param req
     * @return
     */
    BaseRoleDTO save(BaseRoleCreate req);

    /**
     * 修改角色
     * @param req
     * @return
     */
    BaseRoleDTO save(BaseRoleUpdate req);

    /**
     * Get all the baseRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BaseRoleDTO> findAll(Pageable pageable);


    /**
     * Get the "id" baseRole.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BaseRoleDTO> findOne(Long id);

    /**
     * 删除角色
     * @param id
     * @return
     */
    boolean delete(Long id);


    /**
     * 分页角色
     * @param req
     * @return
     */
    PageResult page(BaseRolePage req);

    /**
     * 角色下拉
     * @param req
     * @return
     */
    List<BaseRoleDropDownReq> dropDown(BaseRoleDropDownReq req);


}
