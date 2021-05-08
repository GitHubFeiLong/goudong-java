package com.goudong.oauth2.service;

import com.goudong.commons.entity.AuthorityUserDO;

/**
 * 接口描述：
 * 用户服务层
 * @Author msi
 * @Date 2021-05-02 13:53
 * @Version 1.0
 */
public interface UserService {

    /**
     * 根据qq的openID查询用户
     * @param openID qq互联返回的openID
     * @return
     */
    AuthorityUserDO getUserByQQOpenId(String openID);

    /**
     * 根据 手机号查询用户
     * @param phone 手机号
     * @return
     */
    AuthorityUserDO getUserByPhone(String phone);
}
