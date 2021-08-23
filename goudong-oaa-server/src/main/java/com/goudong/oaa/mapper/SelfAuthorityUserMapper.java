package com.goudong.oaa.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.po.AuthorityUserPO;

import java.util.List;

/**
 * 接口描述：
 * 用户权限
 * @ClassName UserDao
 * @Author msi
 * @Date 2021-04-01 21:11
 * @Version 1.0
 */
public interface SelfAuthorityUserMapper extends BaseMapper<AuthorityUserPO> {

    /**
     * 查询指定用户对应的角色名称
     * @param userId 用户id
     * @return
     */
    List<String> selectRoleNameByUserId(Long userId);
}
