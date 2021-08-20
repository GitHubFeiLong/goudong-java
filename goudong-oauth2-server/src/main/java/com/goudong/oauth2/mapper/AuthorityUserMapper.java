package com.goudong.oauth2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.po.AuthorityUserPO;

import java.util.List;

/**
 * 接口描述：
 *
 * @Author msi
 * @Date 2021-05-02 14:11
 * @Version 1.0
 */
public interface AuthorityUserMapper extends BaseMapper<AuthorityUserPO> {

    /**
     * 新增/修改 用户
     * @param authorityUserPO
     * @return
     */
    int updateInsert(AuthorityUserPO authorityUserPO);


    /**
     * 查询用户的详细信息
     * @param username
     * @return
     */
    AuthorityUserDTO selectUserDetailByUsername(String username);
}
