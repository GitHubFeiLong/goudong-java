package com.goudong.oauth2.dao;


import com.goudong.oauth2.entity.AuthorityMenuDO;

import java.util.List;

/**
 * 菜单持久层
 * @Author msi
 * @Date 2021-04-09 10:26
 * @Version 1.0
 */
public interface AuthorityMenuDao {

    /**
     * 查询需要系统角色才能访问的菜单
     * @return
     */
    List<AuthorityMenuDO> listMenuAndRole();
}
