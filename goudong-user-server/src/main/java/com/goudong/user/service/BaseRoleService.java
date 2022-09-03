package com.goudong.user.service;

import com.goudong.commons.dto.core.BasePageResult;
import com.goudong.user.dto.AddRoleReq;
import com.goudong.user.dto.BaseRole2QueryPageDTO;
import com.goudong.user.dto.BaseRoleDTO;
import com.goudong.user.dto.ModifyRoleReq;
import com.goudong.user.po.BaseRolePO;

import javax.validation.constraints.NotEmpty;
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
    BasePageResult<BaseRoleDTO> page(BaseRole2QueryPageDTO page);

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
}
