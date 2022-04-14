package com.goudong.user.repository;

import com.goudong.user.po.OrderTblPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 接口描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/4/14 22:01
 */
public interface OrderTblRepository extends JpaRepository<OrderTblPO, Integer>, JpaSpecificationExecutor<OrderTblPO> {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

}
