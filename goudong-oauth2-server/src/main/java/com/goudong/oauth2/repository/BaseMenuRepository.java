package com.goudong.oauth2.repository;

import com.goudong.oauth2.po.BaseMenuPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 接口描述：
 * 菜单持久层
 * @author msi
 * @version 1.0
 * @date 2022/1/23 9:13
 */
public interface BaseMenuRepository extends JpaRepository<BaseMenuPO, Long>, JpaSpecificationExecutor<BaseMenuPO> {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 查询所有隐藏菜单
     * @param hide
     * @return
     */
    List<BaseMenuPO> findAllByHide(boolean hide);

}
