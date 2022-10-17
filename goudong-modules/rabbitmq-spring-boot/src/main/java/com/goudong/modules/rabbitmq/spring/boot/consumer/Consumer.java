package com.goudong.modules.rabbitmq.spring.boot.consumer;

import com.goudong.modules.rabbitmq.spring.boot.config.queue.QueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * 类描述：
 * 消费者
 * @author cfl
 * @version 1.0
 * @date 2022/10/17 11:58
 */
@Service
@Slf4j
public class Consumer {
    @RabbitListener(queues = QueueConfig.TEST_QUEUE)
    public void testReceiver(@Payload String messageStr, Channel channel, Message message) throws IOException {
        try {
            log.info("消费者收到消息：{}", messageStr);
            int i = 10 / 0;
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // correlationData null. ack true, cause null（requeue = false， 直接放入死信队列；与basicReject的区别在于可以批量操作）
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            // correlationData null. ack true, cause null （requeue = false， 直接放入死信队列）
            // channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @RabbitListener(queues = QueueConfig.TEST_DEAD_LETTER_QUEUE)
    public void testDeadLetterQueueReceiver(@Payload String messageStr, Channel channel, Message message) throws IOException {
        log.info("死信队列消费者收到消息：{}", messageStr);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
