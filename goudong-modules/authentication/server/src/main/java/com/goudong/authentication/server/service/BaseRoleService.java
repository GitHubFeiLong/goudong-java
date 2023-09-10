package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseRoleCreateReq;
import com.goudong.authentication.server.rest.req.BaseRolePageReq;
import com.goudong.authentication.server.rest.req.BaseRoleUpdateReq;
import com.goudong.authentication.server.rest.req.search.BaseRoleDropDownReq;
import com.goudong.authentication.server.rest.req.search.BaseRolePage;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseRolePageResp;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
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

    /**
     * 分页查询角色列表
     *
     * @param req 条件查询参数
     * @return 角色分页列表
     */
    PageResult<BaseRolePageResp> page(BaseRolePageReq req);

    /**
     * 保存角色
     *
     * @param req 角色信息
     * @return 保存后对象
     */
    BaseRoleDTO save(BaseRoleCreateReq req);

    /**
     * 修改角色
     *
     * @param req 需要修改的角色信息
     * @return 修改后角色信息
     */
    BaseRoleDTO update(BaseRoleUpdateReq req);


    /**
     * 批量删除角色
     *
     * @param ids 删除的id集合
     * @return true删除成功；false删除失败
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 根据id查询角色
     * @param id 角色id
     * @return 角色对象
     */
    BaseRole findById(Long id);




    // 待删除



    /**
     * 修改角色
     * @param req
     * @return
     */
    BaseRoleDTO save(BaseRoleUpdateReq req);

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
     * 查询登录用户所拥有的权限
     * @return 权限集合
     */
    List<BaseMenuDTO> listPermissionsByLoginUser();

}
