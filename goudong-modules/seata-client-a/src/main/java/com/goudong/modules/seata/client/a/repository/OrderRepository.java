package com.goudong.modules.seata.client.a.repository;

import com.goudong.modules.seata.client.a.po.BaseOrderPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 接口描述：
 * 订单持久层
 * @author Administrator
 * @version 1.0
 * @date 2022/8/3 14:06
 */
public interface OrderRepository extends JpaRepository<BaseOrderPO, Long>, JpaSpecificationExecutor<BaseOrderPO> {
    //~methods
    //==================================================================================================================
}
