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

    @Query(value = "from BaseUserPO where username=?1 or email = ?1 or phone=?1")
    BaseUserPO findByLogin(String login);

}
