package com.goudong.file.service;

import com.goudong.file.po.user.BaseRolePO;

import java.util.List;

/**
 * 接口描述：
 * 角色服务层接口
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
public interface BaseRoleService {

    /**
     * 查询所有角色
     * @return
     */
    List<BaseRolePO> findAll();

    /**
     * 根据角色中文名在缓存中查询对象
     * @param roleNameCn 角色中文名
     * @return
     */
    BaseRolePO findByRoleNameCnInCatch(String roleNameCn);
}
