package com.goudong.oauth2.service.impl;

import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.utils.AssertUtil;
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

    /**
     * 根据 手机号查询用户
     *
     * @param phone 手机号
     * @return
     */
    @Override
    public AuthorityUserDO getUserByPhone(String phone) {
        // 检查手机号格式是否正确
        AssertUtil.isPhone(phone, "手机号码格式不正确，获取用户失败");

        return userDao.selectUserByPhone(phone);
    }
}
