package com.goudong.user.controller.seata;

import com.goudong.commons.framework.core.Result;
import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
import com.goudong.user.po.OrderTblPO;
import com.goudong.user.repository.OrderTblRepository;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：
 * seata测试
 * @author msi
 * @date 2022/4/14 21:41
 * @version 1.0
 */
@Api(tags = "seata测试")
@Slf4j
@Validated
@RestController
@RequestMapping("/seata")
public class SeataDemoController {

    /**
     * 订单持久层
     */
    private final OrderTblRepository orderTblRepository;

    private final GoudongOauth2ServerService goudongOauth2ServerService;

    public SeataDemoController(OrderTblRepository orderTblRepository, GoudongOauth2ServerService goudongOauth2ServerService) {
        this.orderTblRepository = orderTblRepository;
        this.goudongOauth2ServerService = goudongOauth2ServerService;
    }

    @PostMapping("/order")
    @ApiOperation("下单")
    // @Transactional
    @GlobalTransactional
    public Result order() {
        // 模拟新增订单
        OrderTblPO orderTblPO = new OrderTblPO();
        orderTblPO.setCount(1);
        orderTblPO.setMoney(10);
        orderTblPO.setUserId("1");
        orderTblRepository.save(orderTblPO);

        // 模拟扣除库存
        goudongOauth2ServerService.delStorage();
        int i = 10 / 0;
        return Result.ofSuccess();
    }
}
