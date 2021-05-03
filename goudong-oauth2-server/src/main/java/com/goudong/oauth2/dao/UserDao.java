package com.goudong.oauth2.dao;

import com.goudong.commons.entity.AuthorityUserDO;

/**
 * 接口描述：
 *
 * @Author msi
 * @Date 2021-05-02 14:11
 * @Version 1.0
 */
public interface UserDao {
    /**
     * 根据openID查询用户
     * @param openID 腾讯互联返回的openId
     * @return
     */
    AuthorityUserDO selectUserByQQOpenId(String openID);
}
