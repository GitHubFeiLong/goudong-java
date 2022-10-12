package com.goudong.modules.rabbitmq.demo.type.work.fair_dispatch;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 工作队列，公平分发，消息消费者1号
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 14:14
 */
public class Recv1 {
    public static final String QUEUE_NAME = "test_fair_dispatch_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false,null);

        channel.basicQos(1);

        // 定义一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            // 消息到达，触发这个方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body, "utf-8");
                System.out.println("Recv [1] msg = " + msg);
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    System.out.println("Recv [1] done!");

                    // 手动回执 false 表示只确认 envelope.DelivertTag 这条消息，true 表示确认 小于等于 b.DelivertTag 的所有消息（批量确认）
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        // 监听队列
        // boolean autoAck = true; //自动应答
        boolean autoAck = false; //手动应答
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
