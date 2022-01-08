package com.goudong.user.repository;

import com.goudong.user.po.BaseRolePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 类描述：
 * 角色持久层
 * @author msi
 * @date 2022/1/8 11:16
 * @version 1.0
 */
public interface BaseRoleRepository extends JpaRepository<BaseRolePO, Long>, JpaSpecificationExecutor<BaseRolePO> {

    /**
     * 根据角色名称，查询角色
     * @param roleName
     * @return
     */
    Optional<BaseRolePO> findByRoleName(String roleName);
}
