package com.goudong.spring.statemachine.demo;

import com.goudong.spring.statemachine.demo.domain.TbOrder;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class EnumTest {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    void test1() {
        EnumSet<TbOrder.OrderStatus> orderStatuses = EnumSet.allOf(TbOrder.OrderStatus.class);
        System.out.println("orderStatuses = " + orderStatuses);
    }
}
