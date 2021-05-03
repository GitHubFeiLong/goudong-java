package com.goudong.oauth2.service.impl;

import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.oauth2.dao.UserDao;
import com.goudong.oauth2.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 类描述：
 * 用户服务层
 * @Author msi
 * @Date 2021-05-02 13:53
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    /**
     * 根据qq的openID查询用户
     *
     * @param openID qq互联返回的openID
     * @return
     */
    @Override
    public AuthorityUserDO getUserByQQOpenId(String openID) {

        return userDao.selectUserByQQOpenId(openID);
    }
}
