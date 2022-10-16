package com.goudong.modules.rabbitmq.demo.dead.letter;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 测试死信队列，消息生产者 测试消费异常
 * @author cfl
 * @version 1.0
 * @date 2022/10/14 20:31
 */
public class Recv3 {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
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
        channel.exchangeDeclare(Send3.EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
        // 申明队列
        channel.queueDeclare(Send3.QUEUE_NAME, durable, false, false, arguments);
        // 绑定
        channel.queueBind(Send3.QUEUE_NAME, Send3.EXCHANGE_NAME, Send2.ROUTING_KEY);

        channel.basicConsume(Send3.QUEUE_NAME, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Recv3接收到消息：" + new String(body));
                // 不重新放入队列
                boolean requeue = false;
                channel.basicReject(envelope.getDeliveryTag(), requeue);
            }
        });
    }
}
