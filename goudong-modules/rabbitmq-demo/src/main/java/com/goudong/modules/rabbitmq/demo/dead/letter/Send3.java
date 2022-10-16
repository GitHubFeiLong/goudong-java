package com.goudong.modules.rabbitmq.demo.dead.letter;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 测试死信队列，消息生产者 测试消费异常
 * @author cfl
 * @version 1.0
 * @date 2022/10/14 16:06
 */
public class Send3 {
    public static final String QUEUE_NAME = "test.queue.3";
    public static final String EXCHANGE_NAME = "test.exchange.3";
    public static final String ROUTING_KEY = "test.routing.key.3";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        boolean durable = true;
        Map<String, Object> arguments = new HashMap<>();
        // 这里声明当前队列绑定的死信交换机
        arguments.put("x-dead-letter-exchange", "test.dead.letter.exchange");
        // 这里声明当前队列的死信路由key
        arguments.put("x-dead-letter-routing-key", "test.dead.letter.routing.key");
        // 申明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
        // 申明队列
        channel.queueDeclare(QUEUE_NAME, durable, false, false, arguments);
        // 绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        // 发送消息
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, "Send3 send message".getBytes("utf-8"));

        channel.close();
        connection.close();
    }
}
