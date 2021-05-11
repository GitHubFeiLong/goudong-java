package com.goudong.oauth2.service;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.po.AuthorityUserPO;

import java.util.List;

/**
 * 接口描述：
 * 用户服务层
 * @Author msi
 * @Date 2021-05-02 13:53
 * @Version 1.0
 */
public interface AuthorityUserService {

    /**
     * 根据 authorityUserPO对象，查询 authority_user表
     * @param authorityUserDTO 用户对象
     * @return
     */
    List<AuthorityUserDTO> listByAuthorityUserDTO(AuthorityUserDTO authorityUserDTO);

    /**
     * 根据指定的用户名，生成3个可以未被注册的用户名
     * 当返回结果为空集合时，表示账号可以使用
     * @param username
     * @return
     */
    List<String> generateUserName(String username);

    /**
     * 新增用户
     * @param authorityUserDO
     * @return
     */
    AuthorityUserDO createUser(AuthorityUserDO authorityUserDO);
}
