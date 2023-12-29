package com.goudong.spring.statemachine.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goudong.spring.statemachine.demo.domain.TbOrder;
import com.goudong.spring.statemachine.demo.mapper.TbOrderMapper;
import com.goudong.spring.statemachine.demo.service.TbOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author chenf
* @description 针对表【tb_order(订单表)】的数据库操作Service实现
* @createDate 2023-12-29 11:24:29
*/
@Service
@Slf4j
public class TbOrderServiceImpl extends ServiceImpl<TbOrderMapper, TbOrder>
    implements TbOrderService{

    @Resource
    private StateMachine<TbOrder.OrderStatus, TbOrder.OrderStatusChangeEvent> orderStateMachine;
    @Resource
    private StateMachinePersister<TbOrder.OrderStatus, TbOrder.OrderStatusChangeEvent, String> stateMachineMemPersister;
    @Resource
    private TbOrderMapper orderMapper;
    /**
     * 创建订单  
     *
     * @param order
     * @return
     */
    public TbOrder create(TbOrder order) {
        order.setStatus(TbOrder.OrderStatus.WAIT_PAYMENT.getKey());
        orderMapper.insert(order);
        return order;
    }
    /**
     * 对订单进行支付  
     *
     * @param id
     * @return
     */
    public TbOrder pay(Long id) {
        TbOrder order = orderMapper.selectById(id);
        log.info("线程名称：{},尝试支付，订单号：{}" ,Thread.currentThread().getName() , id);
        if (!sendEvent(TbOrder.OrderStatusChangeEvent.PAYED, order)) {
            log.error("线程名称：{},支付失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("支付失败, 订单状态异常");
        }
        return order;
    }
    /**
     * 对订单进行发货  
     *
     * @param id
     * @return
     */
    public TbOrder deliver(Long id) {
        TbOrder order = orderMapper.selectById(id);
        log.info("线程名称：{},尝试发货，订单号：{}" ,Thread.currentThread().getName() , id);
        if (!sendEvent(TbOrder.OrderStatusChangeEvent.DELIVERY, order)) {
            log.error("线程名称：{},发货失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("发货失败, 订单状态异常");
        }
        return order;
    }
    /**
     * 对订单进行确认收货  
     *
     * @param id
     * @return
     */
    public TbOrder receive(Long id) {
        TbOrder order = orderMapper.selectById(id);
        log.info("线程名称：{},尝试收货，订单号：{}" ,Thread.currentThread().getName() , id);
        if (!sendEvent(TbOrder.OrderStatusChangeEvent.RECEIVED, order)) {
            log.error("线程名称：{},收货失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("收货失败, 订单状态异常");
        }
        return order;
    }
    /**
     * 发送订单状态转换事件  
     * synchronized修饰保证这个方法是线程安全的  
     *
     * @param changeEvent
     * @param order
     * @return
     */
    private synchronized boolean sendEvent(TbOrder.OrderStatusChangeEvent changeEvent, TbOrder order) {
        boolean result = false;
        try {
            log.info("sendEvent");
            //启动状态机  
            orderStateMachine.start();
            //尝试恢复状态机状态  
            stateMachineMemPersister.restore(orderStateMachine, String.valueOf(order.getId()));
            Message message = MessageBuilder.withPayload(changeEvent).setHeader("order", order).build();
            result = orderStateMachine.sendEvent(message);
            //持久化状态机状态  
            stateMachineMemPersister.persist(orderStateMachine, String.valueOf(order.getId()));
        } catch (Exception e) {
            log.error("订单操作失败:{}", e);
        } finally {
            orderStateMachine.stop();
        }
        return result;
    }
}




