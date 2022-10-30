package com.goudong.user.service;

import com.goudong.core.lang.PageResult;
import com.goudong.user.dto.AddRoleReq;
import com.goudong.user.dto.BaseRole2QueryPageDTO;
import com.goudong.user.dto.BaseRoleDTO;
import com.goudong.user.dto.ModifyRoleReq;
import com.goudong.user.po.BaseRolePO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 接口描述：
 * 角色持久层
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
public interface BaseRoleService {

    /**
     * 查询预置的普通角色
     * @return
     */
    BaseRolePO findByRoleUser();

    /**
     * 分页查询角色
     * @param page
     * @return
     */
    PageResult<BaseRoleDTO> page(BaseRole2QueryPageDTO page);

    /**
     * 根据角色id集合查询角色
     * @param roleIds
     * @return
     */
    List<BaseRoleDTO> listByIds(@NotEmpty List<Long> roleIds);

    /**
     * 新增角色
     * @param req
     * @return
     */
    BaseRoleDTO addRole(AddRoleReq req);

    /**
     * 修改角色
     * @param req
     * @return
     */
    BaseRoleDTO modifyRole(ModifyRoleReq req);

    /**
     * 删除角色
     * @param id
     * @return
     */
    BaseRoleDTO removeRole(Long id);

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    BaseRoleDTO getById(Long id);

    /**
     * 修改角色的权限
     * @param id 角色id
     * @param menuIds 菜单
     */
    BaseRoleDTO updatePermissions(@NotNull Long id, @NotNull List<Long> menuIds);

}
