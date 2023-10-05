package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BaseUserRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseUserRoleRepository extends JpaRepository<BaseUserRole, Long>, JpaSpecificationExecutor<BaseUserRole> {


    // List<BaseUserRole> findAllByUserId(Long userId);

    /**
     * 查询角色有多少用户
     * @param roleId
     * @return
     */
    // int countByRoleId(Long roleId);
}
