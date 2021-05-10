package com.goudong.oauth2.service.impl;

import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.oauth2.dao.UserDao;
import com.goudong.oauth2.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    @Override
    public AuthorityUserDO getUserByUsername(String username) {
        AssertUtil.hasText(username, "根据用户名查询用户时，用户名不能为空");
        return userDao.selectUserByUsername(username);
    }

    /**
     * 根据指定的用户名，生成3个可以未被注册的用户名
     * 当返回结果为空集合时，表示账号可以使用
     * @param username
     * @return
     */
    @Override
    public List<String> generateUserName(String username) {
        AssertUtil.hasText(username, "根据用户名查询用户时，用户名不能为空");
        List<String> result = new ArrayList<>();
        // 查询用户名是否存在
        AuthorityUserDO authorityUserDO = userDao.selectUserByUsername(username);
        if (authorityUserDO == null) {
            return result;
        }

        List<String> names = userDao.selectUserNameByLikeUsername(username);

        Random random = new Random();
        do {
            int i = random.nextInt(10000);
            String item = username + i;
            // 本次生成的用户名不存在
            if(!names.contains(item)){
                names.add(item);
                result.add(item);
            }
        } while (names.size() <= 3);

        return result;
    }

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return
     */
    @Override
    public AuthorityUserDO getUserByEmail(String email) {
        AssertUtil.isEmail(email, "邮箱格式错误");
        return userDao.selectUserByEmail(email);
    }
}
