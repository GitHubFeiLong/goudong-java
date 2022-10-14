package com.goudong.modules.rabbitmq.demo.dead.letter;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 死信队列的消费者
 * @author cfl
 * @version 1.0
 * @date 2022/10/14 16:17
 */
public class DeadLetterRecv {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 申明死信交换机
        channel.exchangeDeclare("test.dead.letter.exchange", "direct", true);
        // 申明死信队列（队列不存在时，会出现异常）
        channel.queueDeclare("test.dead.letter.queue", true, false, false, null);
        // 绑定队列交换机路由key
        channel.queueBind("test.dead.letter.queue", "test.dead.letter.exchange", "test.dead.letter.routing.key", null);

        // 接收消息
        channel.basicConsume("test.dead.letter.queue", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("死信队列接收到消息：" + new String(body));

                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
