package com.goudong.spring.statemachine.demo.config;

import com.goudong.spring.statemachine.demo.domain.TbOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * 类描述：
 * 定义状态机规则和配置状态机
 * @author chenf
 * @version 1.0
 */
@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<TbOrder.OrderStatus, TbOrder.OrderStatusChangeEvent> {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 配置状态
     *
     * @param states
     * @throws Exception
     */
    public void configure(StateMachineStateConfigurer<TbOrder.OrderStatus, TbOrder.OrderStatusChangeEvent> states) throws Exception {
        states
                .withStates()
                .initial(TbOrder.OrderStatus.WAIT_PAYMENT)
                .states(EnumSet.allOf(TbOrder.OrderStatus.class));
    }
    /**
     * 配置状态转换事件关系
     *
     * @param transitions
     * @throws Exception
     */
    public void configure(StateMachineTransitionConfigurer<TbOrder.OrderStatus, TbOrder.OrderStatusChangeEvent> transitions) throws Exception {
        transitions
                //支付事件:待支付-》待发货
                .withExternal().source(TbOrder.OrderStatus.WAIT_PAYMENT).target(TbOrder.OrderStatus.WAIT_DELIVER).event(TbOrder.OrderStatusChangeEvent.PAYED)
                .and()
                //发货事件:待发货-》待收货
                .withExternal().source(TbOrder.OrderStatus.WAIT_DELIVER).target(TbOrder.OrderStatus.WAIT_RECEIVE).event(TbOrder.OrderStatusChangeEvent.DELIVERY)
                .and()
                //收货事件:待收货-》已完成
                .withExternal().source(TbOrder.OrderStatus.WAIT_RECEIVE).target(TbOrder.OrderStatus.FINISH).event(TbOrder.OrderStatusChangeEvent.RECEIVED);
    }
}