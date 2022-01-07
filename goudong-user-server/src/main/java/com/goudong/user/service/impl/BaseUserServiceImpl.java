package com.goudong.user.service.impl;

import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.user.service.BaseUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 用户服务层
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
public class BaseUserServiceImpl implements BaseUserService {

    /**
     * 根据指定的用户名，生成3个可以未被注册的用户名
     * 当返回结果为空集合时，表示账号可以使用
     *
     * @param username
     * @return
     */
    @Override
    public List<String> generateUserName(String username) {
        AssertUtil.hasText(username, "根据用户名查询用户时，用户名不能为空");
        List<String> result = new ArrayList<>();
        // 查询用户名是否存在
        LambdaQueryWrapper<AuthorityUserPO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(AuthorityUserPO::getUsername, username);

        AuthorityUserPO authorityUserPO = super.getOne(lambdaQueryWrapper);

        if (authorityUserPO == null) {
            return result;
        }

        lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(AuthorityUserPO::getUsername, username);

        List<String> names = super.list(lambdaQueryWrapper).stream().map(AuthorityUserPO::getUsername).collect(Collectors.toList());

        Random random = new Random();
        do {
            int i = random.nextInt(10000);
            String item = username + i;
            // 本次生成的用户名不存在
            if(!names.contains(item)){
                names.add(item);
                result.add(item);
            }
        } while (result.size() < 3);

        return result;
    }
}
