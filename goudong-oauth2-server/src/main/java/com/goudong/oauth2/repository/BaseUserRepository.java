package com.goudong.oauth2.repository;

import com.goudong.oauth2.po.BaseUserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 接口描述：
 * 用户持久层
 * @author msi
 * @version 1.0
 * @date 2022/1/7 21:03
 */
public interface BaseUserRepository extends JpaRepository<BaseUserPO, Long>, JpaSpecificationExecutor<BaseUserPO> {

    @Query(value = "from BaseUserPO where appId=?1 and (username=?2 or email = ?2 or phone=?2)")
    BaseUserPO findByLogin(Long appId, String login);

    /**
     * 根据qq的openId查询用户信息
     * @param qqOpenId
     * @return
     */
    // BaseUserPO findByQqOpenId(String qqOpenId);

    /**
     * 查询最大admin用户id
     * @return
     */
    @Query(nativeQuery = true, value = "select id from base_user where id <= 2147483647 and deleted = false order by id desc limit 1")
    Long findMaxAdminUserId();
}
