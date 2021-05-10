package com.goudong.oauth2.service;

import com.goudong.commons.entity.InvalidEmailDO;

/**
 * 无效邮箱，服务层
 * @Author msi
 * @Date 2021-05-10 9:52
 * @Version 1.0
 */
public interface InvalidEmailService {
    /**
     * 添加一条无效邮箱地址
     * @param email
     * @return
     */
    int add (String email);

    /**
     * 根据邮箱查询
     * @param email
     * @return
     */
    InvalidEmailDO getByEmail(String email);
}
