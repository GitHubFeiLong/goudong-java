package com.goudong.oauth2.dao;

import com.goudong.commons.po.AuthorityRolePO;

import java.util.List;

/**
 * 接口描述：
 *
 * @Author msi
 * @Date 2021-05-02 14:11
 * @Version 1.0
 */
public interface AuthorityRoleDao {

    /**
     * 查询角色
     */
    List<AuthorityRolePO> select(AuthorityRolePO authorityRolePO);

    /**
     * 新增角色
     * @param authorityRolePO
     * @return
     */
    int insert(AuthorityRolePO authorityRolePO);

}
