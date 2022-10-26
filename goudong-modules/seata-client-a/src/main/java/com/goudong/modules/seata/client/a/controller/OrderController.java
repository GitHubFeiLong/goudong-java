package com.goudong.modules.seata.client.a.controller;

import cn.hutool.core.util.IdUtil;
import com.goudong.boot.exception.core.Result;
import com.goudong.commons.framework.openfeign.SeataClientBService;
import com.goudong.modules.seata.client.a.po.BaseOrderPO;
import com.goudong.modules.seata.client.a.repository.OrderRepository;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/8/3 14:05
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    //~fields
    //==================================================================================================================
    private final OrderRepository orderRepository;

    private final SeataClientBService seataClientBService;

    private final RestTemplate restTemplate = new RestTemplate();
    //~methods
    //==================================================================================================================

    @PostMapping("/save")
    @GlobalTransactional
    public Result<BaseOrderPO> save(@RequestBody BaseOrderPO orderPO) {
        orderPO.setId(IdUtil.getSnowflake(1,1).nextId());
        orderRepository.save(orderPO);

        // 调用库存服务减少库存
        seataClientBService.save(orderPO.getProduct());

        // 模仿异常
        double d = 10 / 0;

        return Result.ofSuccess(orderPO);
    }

}
