package com.goudong.user.repository;

import com.goudong.user.po.BaseRolePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
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
     * @param appId
     * @param roleName
     * @return
     */
    Optional<BaseRolePO> findByAppIdAndRoleName(Long appId, String roleName);

    /**
     * 查询应用下的指定id的所有角色
     * @param appId
     * @param ids
     * @return
     */
    List<BaseRolePO> findAllByAppIdAndIdIn(Long appId, List<Long> ids);

    /**
     * 根据角色名查询
     * @param roleNameCn
     * @return
     */
    Optional<BaseRolePO> findByRoleNameCn(String roleNameCn);
}
