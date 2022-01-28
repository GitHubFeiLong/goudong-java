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
     * @param phone
     * @return
     */
    BaseUserPO findByPhone(String phone);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    BaseUserPO findByUsername(String username);

    /**
     * 根据用户名模糊查询
     * @param username
     * @return
     */
    List<BaseUserPO> findAllByUsernameIsLike(String username);

    /**
     * 根据邮箱查询用户
     * @param email
     * @return
     */
    BaseUserPO findByEmail(String email);

    @Query(value = "from BaseUserPO where username=?1 or email = ?1 or phone=?1")
    BaseUserPO findByLogin(String login);

    /**
     * 根据qq的openId查询用户信息
     * @param qqOpenId
     * @return
     */
    BaseUserPO findByQqOpenId(String qqOpenId);
}
