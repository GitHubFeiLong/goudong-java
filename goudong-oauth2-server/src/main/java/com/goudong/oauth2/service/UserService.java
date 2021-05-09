package com.goudong.oauth2.service;

import com.goudong.commons.entity.AuthorityUserDO;

import java.util.List;

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

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    AuthorityUserDO getUserByUsername(String username);

    /**
     * 根据指定的用户名，生成3个可以未被注册的用户名
     * 当返回结果为空集合时，表示账号可以使用
     * @param username
     * @return
     */
    List<String> generateUserName(String username);
}
