package com.goudong.oauth2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goudong.commons.po.AuthorityUserPO;

import java.util.List;

/**
 * 接口描述：
 *
 * @Author msi
 * @Date 2021-05-02 14:11
 * @Version 1.0
 */
public interface AuthorityUserMapper extends BaseMapper<AuthorityUserPO> {

    /**
     * 根据实体对象条件查询用户表，并且条件拼接
     * @param authorityUserPO
     * @return
     */
    List<AuthorityUserPO> selectByAnd(AuthorityUserPO authorityUserPO);

    /**
     * 根据实体对象条件查询用户表，或者条件拼接
     * @param authorityUserPO
     * @return
     */
    @Deprecated
    List<AuthorityUserPO> selectByOr(AuthorityUserPO authorityUserPO);

    /**
     * 根据用户名 模糊查询已存在的账号名称
     * @param username
     * @return
     */
    @Deprecated
    List<String> selectUserNameByLikeUsername(String username);

    /**
     * 新增/修改 用户
     * @param authorityUserPO
     * @return
     */
    int updateInsert(AuthorityUserPO authorityUserPO);

    /**
     * 只修改对象中有效值
     * @param userPO 用户对象
     * @return
     */
    @Deprecated
    int updateByPatch(AuthorityUserPO userPO);
}
