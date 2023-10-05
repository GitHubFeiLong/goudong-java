package com.goudong.authentication.server.repository;

import com.goudong.authentication.server.domain.BaseMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the BaseMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaseMenuRepository extends JpaRepository<BaseMenu, Long>, JpaSpecificationExecutor<BaseMenu> {

    /**
     * 删除应用下的所有菜单
     * @param appId
     * @return
     */
    @Modifying
    @Query(nativeQuery = true, value = "delete from base_menu where app_id = ?1")
    int deleteByAppId(Long appId);

    /**
     * 查询应用下所有菜单
     * @param appId 应用id
     * @return 菜单集合
     */
    List<BaseMenu> findAllByAppId(Long appId);

    /**
     * 修改排序
     * @param appId 应用id
     * @param parentId 父级菜单id
     * @param minSortNum 排序最小
     * @param maxSortNum 排序最大
     * @param incr 增量
     */
    @Modifying
    @Query(nativeQuery = true, value = "update base_menu set sort_num = sort_num + ?5 where app_id=?1 and parent_id=?2 and sort_num >= ?3 and sort_num <= ?4 ")
    void updateSortNum(Long appId, Long parentId, Integer minSortNum, Integer maxSortNum, int incr);
}
