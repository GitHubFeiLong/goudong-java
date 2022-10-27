package com.goudong.modules.seata.client.b.controller;

import com.google.common.collect.Lists;
import com.goudong.boot.exception.core.ClientException;
import com.goudong.boot.exception.enumerate.ClientExceptionEnum;
import com.goudong.core.lang.Result;
import com.goudong.modules.seata.client.b.po.WarehousePO;
import com.goudong.modules.seata.client.b.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/8/3 14:05
 */
@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
    //~fields
    //==================================================================================================================
    private final WarehouseRepository warehouseRepository;

    //~methods
    //==================================================================================================================

    @GetMapping("/init")
    public Result<List<WarehousePO>> init() {
        warehouseRepository.deleteAll();
        List<WarehousePO> list = Lists.newArrayList(
               new WarehousePO(1L, "pc", BigDecimal.valueOf(100)),
               new WarehousePO(2L, "mobile", BigDecimal.valueOf(100))
        );

        warehouseRepository.saveAll(list);
        return Result.ofSuccess(list);

    }

    //@GetMapping("/save")
    @DeleteMapping("/del-warehouse")
    public Result<WarehousePO> save(String product) {

        WarehousePO warehousePO = warehouseRepository.findByProduct(product)
                .orElseThrow(() -> ClientException.client(ClientExceptionEnum.BAD_REQUEST, "产品不存在库存"));

        // 减少1
        warehousePO.setNumber(warehousePO.getNumber().subtract(BigDecimal.ONE));

        // 手动保存，事务和seata的事务有影响，会出错。
        warehouseRepository.save(warehousePO);
        return Result.ofSuccess(warehousePO);
    }

}
