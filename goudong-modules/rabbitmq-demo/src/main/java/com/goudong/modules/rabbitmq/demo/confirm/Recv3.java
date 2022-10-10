package com.goudong.modules.rabbitmq.demo.confirm;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * confirm机制，消息消费者
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 17:33
 */
public class Recv3 {

    public static final String QUEUE_NAME = "test_queue_confirm3";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();

        // 创建通道
        Channel channel = connection.createChannel();

        // 队列声明
        channel.queueDeclare(QUEUE_NAME, false, false,false, null);

        // 定义队列的消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            // 获取到达的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msgString = new String(body,"utf-8");
                System.out.println("new api recv: " + msgString);
            }
        };

        // 监听队列
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);

    }
}
