package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BaseRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseRoleRepository extends JpaRepository<BaseRole, Long>, JpaSpecificationExecutor<BaseRole> {

    /**
     * 查询角色
     * @return
     */
    @Query("from BaseRole where name='ROLE_APP_ADMIN'")
    BaseRole findByAppAdmin();

    /**
     * 删除应用下的所有角色
     * @param appId
     * @return
     */
    @Modifying
    @Query(nativeQuery = true, value = "delete from base_role where app_id = ?1")
    int deleteByAppId(Long appId);

    /**
     * 查询指定应用的管理员角色
     * @param appId 应用id
     * @return 角色对象
     */
    @Query("from BaseRole where appId=?1 and name='ROLE_APP_ADMIN'")
    BaseRole findAppAdminByAppId(Long appId);
}
