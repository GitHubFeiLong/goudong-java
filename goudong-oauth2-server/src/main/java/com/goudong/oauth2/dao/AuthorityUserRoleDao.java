package com.goudong.oauth2.dao;

import java.util.List;

import com.goudong.commons.po.AuthorityUserRolePO;
import org.apache.ibatis.annotations.Param;

/**
 * 类描述：
 * 用户角色中间表
 * @Author msi
 * @Date 2021-05-13 22:08
 * @Version 1.0
 */
public interface AuthorityUserRoleDao {

    /**
     * 新增绑定角色
     * @param record
     * @return
     */
    int insert(AuthorityUserRolePO record);
}
