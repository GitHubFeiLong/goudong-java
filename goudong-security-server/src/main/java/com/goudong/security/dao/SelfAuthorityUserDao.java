package com.goudong.security.dao;




import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.po.AuthorityUserPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 接口描述：
 * 用户权限
 * @ClassName UserDao
 * @Author msi
 * @Date 2021-04-01 21:11
 * @Version 1.0
 */
public interface SelfAuthorityUserDao {

    /**
     * 根据用户名/手机号/邮箱查询正常的用户
     * @param userPO 用户对象
     * @return
     */
    List<AuthorityUserPO> selectUser(AuthorityUserPO userPO);

    /************/
    /**
     * 根据用户名/手机号/邮箱查询正常的用户
     * @param username
     * @return
     */
    AuthorityUserPO selectUserByUsername(String username);
    /**
     * 查询指定用户对应的角色名称
     * @param uuid
     * @return
     */
    List<String> selectRoleNameByUserUuid(String uuid);

    /**
     * 查询用户的基本信息
     * @param username
     * @return
     */
    AuthorityUserDTO selectUserDetailByUsername(String username);

    /**
     * 根据请求路径和请求方式查询需要的角色
     * @param requestUrl
     * @param requestMethod
     * @return
     */
    List<String> selectRoleNameByMenu(String requestUrl, String requestMethod);
}
