package com.goudong.user.service;

import com.goudong.commons.dto.AuthorityUserDTO;

import java.util.List;

/**
 * 接口描述：
 * 用户服务层接口
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
public interface BaseUserService {
    /**
     * 根据指定的用户名，生成3个可以未被注册的用户名
     * 当返回结果为空集合时，表示账号可以使用
     * @param username
     * @return
     */
    List<String> generateUserName(String username);

    // /**
    //  * 新增用户
    //  * @param authorityUserDTO
    //  * @return
    //  */
    // AuthorityUserDTO createUser(AuthorityUserDTO authorityUserDTO);
    //
    // /**
    //  * 根据 登录账号，查询用户基本信息
    //  * @param loginName 登录账号
    //  * @return
    //  */
    // AuthorityUserDTO getUserByLoginName(String loginName);
    //
    // /**
    //  * 绑定opendId
    //  * @param userDTO
    //  */
    // AuthorityUserDTO updateOpenId(AuthorityUserDTO userDTO);
    //
    // /**
    //  * 根据用户名或者电话号或者邮箱 查询用户的详细信息。包含用户信息，角色信息，权限信息
    //  * @param loginName
    //  * @return
    //  */
    // AuthorityUserDTO getUserDetailByLoginName(String loginName);
    //
    // /**
    //  * 更新密码
    //  * @param authorityUserDTO
    //  * @return
    //  */
    // AuthorityUserDTO updatePassword(AuthorityUserDTO authorityUserDTO);
    //
    // AuthorityUserDTO demo(String name, String address, int i);
}