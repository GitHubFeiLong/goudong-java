package com.goudong.oauth2.service.impl;

import com.goudong.commons.entity.InvalidEmailDO;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.oauth2.dao.InvalidEmailDao;
import com.goudong.oauth2.service.InvalidEmailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author msi
 * @Date 2021-05-10 9:53
 * @Version 1.0
 */
@Service
public class InvalidEmailServiceImpl implements InvalidEmailService {
    @Resource
    private InvalidEmailDao invalidEmailDao;

    /**
     * 添加一条无效邮箱地址
     *
     * @param email
     * @return
     */
    @Override
    public int add(String email) {
        AssertUtil.isEmail(email, "邮箱格式错误");
        return invalidEmailDao.insert(email);
    }

    /**
     * 根据邮箱查询
     *
     * @param email
     * @return
     */
    @Override
    public InvalidEmailDO getByEmail(String email) {
        return invalidEmailDao.selectByEmail(email);
    }
}
