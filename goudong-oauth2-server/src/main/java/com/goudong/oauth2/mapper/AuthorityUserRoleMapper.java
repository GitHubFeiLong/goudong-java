package com.goudong.oauth2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goudong.commons.po.AuthorityUserRolePO;

/**
 * 类描述：
 * 用户角色中间表
 * @Author msi
 * @Date 2021-05-13 22:08
 * @Version 1.0
 */
public interface AuthorityUserRoleMapper extends BaseMapper<AuthorityUserRolePO> {

    /**
     * 新增绑定角色
     * @param record
     * @return
     */
//    @Deprecated
//    int insert(AuthorityUserRolePO record);
}
