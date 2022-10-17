package com.goudong.modules.rabbitmq.spring.boot.producer;

import com.goudong.modules.rabbitmq.spring.boot.config.binding.BindingConfig;
import com.goudong.modules.rabbitmq.spring.boot.config.exchange.ExchangeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 类描述：
 * 消息发送者
 * @author cfl
 * @version 1.0
 * @date 2022/10/17 11:06
 */
@Service
@Slf4j
public class TestProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     */
    public void testSend() {
        rabbitTemplate.setConfirmCallback(this);
        // rabbitTemplate.setReturnCallback(this);
        log.info("生产者开始发送消息");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = LocalDateTime.now().format(dateTimeFormatter);
        String message = "这是发送的消息,发送时间：" + format;
        rabbitTemplate.convertAndSend(ExchangeConfig.TEST_DIRECT_EXCHANGE, BindingConfig.TEST_ROUTING_KEY, message);
        log.info("生产者结束发送消息");
    }

    /**
     * 测试发送错误的路由key
     */
    public void testSendInexistenceRoutingKey() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
        log.info("生产者开始发送消息");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = LocalDateTime.now().format(dateTimeFormatter);
        String message = "这是发送的消息,发送时间：" + format;
        rabbitTemplate.convertAndSend(ExchangeConfig.TEST_DIRECT_EXCHANGE, "inexistence.routing.key", message);
        log.info("生产者结束发送消息");
    }


    /**
     * 消息发送成功的确认回调,
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("correlationData {}. ack {}, cause {}", correlationData, ack, cause);
    }

    /**
     * 消息发送到RabbitMQ时，发现没有队列和发送时的交换机，路由键匹配时的回调
     * Returned message callback.
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("{}\n{}\n{}\n{}\n{}", message, replyCode, replyText, exchange, routingKey);
    }
}
