package com.goudong.modules.rabbitmq.demo.dead.letter;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/14 16:06
 */
public class Send1 {
    public static final String QUEUE_NAME = "test.queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        boolean durable = true;
        Map<String, Object> arguments = new HashMap<>();
        // 这里声明当前队列绑定的死信交换机
        arguments.put("x-dead-letter-exchange", "test.dead.letter.exchange");
        // 这里声明当前队列的死信路由key
        arguments.put("x-dead-letter-routing-key", "test.dead.letter.routing.key");
        // 这里设置队列中所有消息的有效期是5s
        arguments.put("x-message-ttl", 5000);
        channel.queueDeclare(QUEUE_NAME, durable, false, false, arguments);

        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, "send message".getBytes("utf-8"));

        channel.close();
        connection.close();
    }
}
