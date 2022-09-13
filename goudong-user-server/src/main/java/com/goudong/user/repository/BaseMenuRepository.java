package com.goudong.user.repository;

import com.goudong.user.po.BaseMenuPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 类描述：
 * 菜单持久层接口
 * @author cfl
 * @date 2022/9/13 21:04
 * @version 1.0
 */
public interface BaseMenuRepository extends JpaRepository<BaseMenuPO, Long>, JpaSpecificationExecutor<BaseMenuPO> {

}
