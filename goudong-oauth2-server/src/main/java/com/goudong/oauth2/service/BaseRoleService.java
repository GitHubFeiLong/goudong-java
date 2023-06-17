package com.goudong.oauth2.service;


import com.goudong.oauth2.dto.authentication.BaseUserDTO;

import java.util.List;

/**
 * 类描述：
 * 角色服务层接口
 * @author cfl
 * @date 2022/9/17 21:47
 * @version 1.0
 */
public interface BaseRoleService {

    /**
     * 填充角色和菜单
     * @param ids
     * @param user
     * @return
     */
    void fillRoleAndMenu(List<Long> ids, BaseUserDTO user);
}

