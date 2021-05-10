package com.goudong.oauth2.dao;

import com.goudong.commons.entity.InvalidEmailDO;

/**
 * invalid_email 持久层
 * @Author msi
 * @Date 2021-05-10 9:54
 * @Version 1.0
 */
public interface InvalidEmailDao {
    /**
     * 插入一条无效邮箱
     * @param email
     * @return
     */
    int insert(String email);

    /**
     * 根据邮箱查询
     * @param email
     * @return
     */
    InvalidEmailDO selectByEmail(String email);
}
