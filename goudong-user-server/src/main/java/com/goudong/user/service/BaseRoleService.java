package com.goudong.user.service;

import com.goudong.commons.dto.core.BasePageResult;
import com.goudong.user.dto.BaseRole2QueryPageDTO;
import com.goudong.user.dto.BaseRoleDTO;
import com.goudong.user.po.BaseRolePO;

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
}
