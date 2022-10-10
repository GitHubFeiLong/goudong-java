package com.goudong.modules.rabbitmq.demo.type.routing;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 路由模式队列，消息消费者1号
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 15:13
 */
public class Recv1 {
    public static final String EXCHANGE_NAME = "test_exchange_direct";

    public static final String QUEUE_NAME = "test_queue_direct_1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);

        // 绑定队列到交换机，并绑定 routingKey
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");

        // 保证一次只分发一个
        channel.basicQos(1);

        // 定义一个消费者
        Consumer consumer = new DefaultConsumer(channel){
            // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("Recv [1] msg = " + msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally{
                    // 消息回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    System.out.println("Recv [1] done!");
                }
            }
        };

        // 监听队列 autoAck(消息应答):false 手动回执 (消息回执 channel.basicAck(envelope.getDeliveryTag(), false);)
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
