package com.goudong.oauth2.service;


import com.goudong.commons.dto.oauth2.BaseRoleDTO;

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
     * 查询角色集合
     * @param ids
     * @return
     */
    List<BaseRoleDTO> listByIds(List<Long> ids);
}

