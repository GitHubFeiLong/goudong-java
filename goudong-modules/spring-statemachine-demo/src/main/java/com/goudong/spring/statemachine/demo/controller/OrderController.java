package com.goudong.spring.statemachine.demo.controller;

import com.goudong.spring.statemachine.demo.domain.TbOrder;
import com.goudong.spring.statemachine.demo.service.TbOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单接口")
public class OrderController {
    @Resource
    private TbOrderService orderService;
    /**
     * 根据id查询订单
     *
     * @return
     */
    @GetMapping("/getById")
    @ApiOperation("获取订单")
    public TbOrder getById(@RequestParam("id") Long id) {
        //根据id查询订单
        TbOrder order = orderService.getById(id);
        return order;
    }
    /**
     * 创建订单
     *
     * @return
     */
    @PostMapping("/create")
    @ApiOperation("创建订单")
    public TbOrder create(@RequestBody TbOrder order) {
        //创建订单
        orderService.create(order);
        return order;
    }
    /**
     * 对订单进行支付
     *
     * @param id
     * @return
     */
    @PostMapping("/pay")
    @ApiOperation("对订单进行支付")
    public String pay(@RequestParam("id") Long id) {
        //对订单进行支付
        orderService.pay(id);
        return "success";
    }

    /**
     * 对订单进行发货
     *
     * @param id
     * @return
     */
    @PostMapping("/deliver")
    @ApiOperation("对订单进行发货")
    public String deliver(@RequestParam("id") Long id) {
        //对订单进行确认收货
        orderService.deliver(id);
        return "success";
    }
    /**
     * 对订单进行确认收货
     *
     * @param id
     * @return
     */
    @PostMapping("/receive")
    @ApiOperation("对订单进行确认收货")
    public String receive(@RequestParam("id") Long id) {
        //对订单进行确认收货
        orderService.receive(id);
        return "success";
    }
}
