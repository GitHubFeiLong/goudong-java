package com.goudong.modules.rabbitmq.spring.boot.controller;

import com.goudong.modules.rabbitmq.spring.boot.producer.TestProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 类描述：
 * 发送消息控制层
 * @author cfl
 * @version 1.0
 * @date 2022/10/17 14:29
 */
@RequestMapping("/send")
@RestController
public class TestController {

    @Resource
    private TestProducer testProducer;

    @GetMapping
    public String send() {
        // testProducer.testSend();
        testProducer.testSendInexistenceRoutingKey();
        return "发送成功";
    }
}
