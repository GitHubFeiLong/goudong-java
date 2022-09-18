package com.goudong.user.repository;

import com.goudong.user.po.BaseMenuPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 类描述：
 * 菜单持久层接口
 * @author cfl
 * @date 2022/9/13 21:04
 * @version 1.0
 */
public interface BaseMenuRepository extends JpaRepository<BaseMenuPO, Long>, JpaSpecificationExecutor<BaseMenuPO> {

    Integer countBySys(boolean sys);

    @Modifying
    @Query(nativeQuery = true, value = "delete from base_role_menu where menu_id in (:menuIds);")
    void deleteRoleMenu(@Param("menuIds") List<Long> menuIds);

    /**
     * 根据id删除
     * @param menuIds
     * @return
     */
    @Modifying
    @Query(nativeQuery = true, value = "delete from base_menu where id in (:menuIds);")
    void deleteByIdIn(@Param("menuIds") List<Long> menuIds);
}
