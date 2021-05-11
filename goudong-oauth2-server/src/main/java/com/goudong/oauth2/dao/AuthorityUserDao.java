package com.goudong.oauth2.dao;

import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.po.AuthorityUserPO;

import java.util.List;

/**
 * 接口描述：
 *
 * @Author msi
 * @Date 2021-05-02 14:11
 * @Version 1.0
 */
public interface AuthorityUserDao {

    /**
     * 根据实体对象条件查询用户表
     * @param authorityUserPO
     * @return
     */
    List<AuthorityUserPO> select(AuthorityUserPO authorityUserPO);

    /**
     * 根据用户名 模糊查询已存在的账号名称
     * @param username
     * @return
     */
    List<String> selectUserNameByLikeUsername(String username);


    /**
     * 新增用户
     * @param authorityUserPO
     * @return
     */
    int insert(AuthorityUserPO authorityUserPO);
}
