package com.goudong.user.service;

import com.goudong.core.lang.PageResult;
import com.goudong.user.dto.AdminEditUserReq;
import com.goudong.user.dto.BaseUser2QueryPageDTO;
import com.goudong.user.dto.BaseUserDTO;
import com.goudong.user.dto.SimpleCreateUserReq;
import com.goudong.user.po.BaseUserPO;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 新增用户
     * @param baseUserDTO
     * @return
     */
    BaseUserDTO createUser(BaseUserDTO baseUserDTO);

    /**
     * 根据 用户名、手机号、邮箱 查询用户基本信息
     * @param loginName 用户名、手机号、邮箱
     * @return
     */
    List<BaseUserPO> getUserByLoginName(String loginName);

    /**
     * 绑定opendId
     * @param userDTO
     */
    BaseUserDTO updateOpenId(BaseUserDTO userDTO);

    /**
     * 根据用户名或者电话号或者邮箱 查询用户的详细信息。包含用户信息，角色信息，权限信息
     * @param loginName
     * @return
     */
    BaseUserDTO getUserDetailByLoginName(String loginName);

    /**
     * 更新密码
     * @param baseUserDTO
     * @return
     */
    BaseUserDTO updatePassword(BaseUserDTO baseUserDTO);

    /**
     * 根据某个字段进行分页
     * @param page
     * @return
     */
    PageResult<BaseUserDTO> pageByField(BaseUser2QueryPageDTO page);

    /**
     * 用户分页查询
     * @param page
     * @return
     */
    PageResult<com.goudong.commons.dto.oauth2.BaseUserDTO> page(BaseUser2QueryPageDTO page);

    /**
     * 根据id查询
     * @param ids
     * @return
     */
    List<com.goudong.commons.dto.oauth2.BaseUserDTO> findAllById(List<Long> ids);

    /**
     * 后台简单新增一个用户
     * @param createDTO
     * @return
     */
    BaseUserDTO simpleCreateUser(SimpleCreateUserReq createDTO);

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    com.goudong.commons.dto.oauth2.BaseUserDTO getUserById(Long id);

    /**
     * admin平台修改用户信息
     * @param req
     * @return
     */
    BaseUserDTO adminEditUser(AdminEditUserReq req);

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    BaseUserDTO deleteUserById(Long id);

    /**
     * 重置用户密码
     * @param id
     * @return
     */
    @Transactional
    boolean resetPassword(Long id);

    /**
     * 修改用户激活状态
     * @param id
     * @return
     */
    @Transactional
    boolean changeEnabled(Long id);

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    @Transactional
    boolean deleteUserByIds(List<Long> ids);
}
