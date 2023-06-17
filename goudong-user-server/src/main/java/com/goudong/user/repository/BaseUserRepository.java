package com.goudong.user.repository;

import com.goudong.user.po.BaseUserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 接口描述：
 * 用户持久层
 * @author msi
 * @version 1.0
 * @date 2022/1/7 21:03
 */
public interface BaseUserRepository extends JpaRepository<BaseUserPO, Long>, JpaSpecificationExecutor<BaseUserPO> {

    /**
     * 根据手机号查询用户
     * @param appId
     * @param phone
     * @return
     */
    BaseUserPO findByAppIdAndPhone(Long appId, String phone);

    /**
     * 根据用户名查询用户
     * @param username
     * @param appId
     * @return
     */
    BaseUserPO findByAppIdAndUsername(Long appId, String username);

    /**
     * 根据用户名模糊查询
     * @param appId
     * @param username
     * @return
     */
    List<BaseUserPO> findAllByAppIdAndUsernameIsLike(Long appId, String username);

    /**
     * 根据邮箱查询用户
     * @param appId
     * @param email
     * @return
     */
    BaseUserPO findByAppIdAndEmail(Long appId, String email);

    /**
     * 根据登录账号查询用户
     * @param appId
     * @param login
     * @return
     */
    @Query(value = "from BaseUserPO where appId= ?1 and (username=?2 or email = ?2 or phone=?2)")
    BaseUserPO findByLogin(Long appId, String login);

    int deleteByIdIn(List<Long> ids);
}
