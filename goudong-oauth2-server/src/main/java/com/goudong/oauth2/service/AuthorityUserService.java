package com.goudong.oauth2.service;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.po.AuthorityUserPO;

import java.util.List;

/**
 * 接口描述：
 * 用户服务层
 * @Author msi
 * @Date 2021-05-02 13:53
 * @Version 1.0
 */
public interface AuthorityUserService {

    /**
     * 根据 AuthorityUserDTO对象，使用逻辑与条件 查询 authority_user表
     * @param authorityUserDTO 用户对象
     * @return
     */
    List<AuthorityUserDTO> listByAndAuthorityUserDTO(AuthorityUserDTO authorityUserDTO);

    /**
     * 根据指定的用户名，生成3个可以未被注册的用户名
     * 当返回结果为空集合时，表示账号可以使用
     * @param username
     * @return
     */
    List<String> generateUserName(String username);

    /**
     * 新增用户
     * @param authorityUserDTO
     * @return
     */
    AuthorityUserDTO createUser(AuthorityUserDTO authorityUserDTO);

    /**
     * 根据 AuthorityUserDTO对象，使用逻辑或条件 查询 authority_user表
     * @param authorityUserDTO 用户对象
     * @return
     */
    List<AuthorityUserDTO> listByOrAuthorityUserDTO(AuthorityUserDTO authorityUserDTO);

    /**
     * patch方式修改用户信息，只有有值才进行修改
     * @param userDTO
     * @return
     */
    AuthorityUserDTO updateByPatch(AuthorityUserDTO userDTO);

    /**
     * 绑定opendId
     * @param userDTO
     */
    AuthorityUserDTO updateOpenId(AuthorityUserDTO userDTO);

}
