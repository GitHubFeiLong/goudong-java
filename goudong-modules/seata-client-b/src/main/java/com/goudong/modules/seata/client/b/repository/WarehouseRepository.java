package com.goudong.modules.seata.client.b.repository;

import com.goudong.modules.seata.client.b.po.WarehousePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 接口描述：
 * 订单持久层
 * @author Administrator
 * @version 1.0
 * @date 2022/8/3 14:06
 */
public interface WarehouseRepository extends JpaRepository<WarehousePO, Long>, JpaSpecificationExecutor<WarehousePO> {
    //~methods
    //==================================================================================================================
    Optional<WarehousePO> findByProduct(String product);
}
