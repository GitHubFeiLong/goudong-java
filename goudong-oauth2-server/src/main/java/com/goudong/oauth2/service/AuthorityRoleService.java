package com.goudong.oauth2.service;

import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.po.AuthorityRolePO;
import com.goudong.commons.pojo.PageParam;
import com.goudong.commons.vo.AuthorityRoleVO;

import java.util.List;

/**
 * 接口描述：
 * 角色服务层接口
 * @Author msi
 * @Date 2021-08-19 20:46
 * @Version 1.0
 */
public interface AuthorityRoleService extends IService<AuthorityRolePO> {

    /**
     * 批量添加角色
     * @param authorityRoleDTOS
     * @return
     */
    List<AuthorityRoleDTO> addRoles(List<AuthorityRoleDTO> authorityRoleDTOS);

    /**
     * 新增角色
     * @param authorityRoleDTO
     * @return
     */
    AuthorityRoleDTO addRole(AuthorityRoleDTO authorityRoleDTO);

    /**
     * 更新角色信息
     * @param authorityRoleDTO
     * @return
     */
    AuthorityRoleDTO updateRole(AuthorityRoleDTO authorityRoleDTO);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    AuthorityRoleDTO deleteRole(Long id);


    Page<AuthorityRolePO> pageRoles(PageParam page);

}
