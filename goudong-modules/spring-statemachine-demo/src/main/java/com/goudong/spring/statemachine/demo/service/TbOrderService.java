package com.goudong.spring.statemachine.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.goudong.spring.statemachine.demo.domain.TbOrder;

/**
* @author chenf
* @description 针对表【tb_order(订单表)】的数据库操作Service
* @createDate 2023-12-29 11:24:29
*/
public interface TbOrderService extends IService<TbOrder> {

    TbOrder create(TbOrder order);
    TbOrder pay(Long id);
    TbOrder deliver(Long id);

    TbOrder receive(Long id);
}
