package com.goudong.modules.rabbitmq.demo.type.simple;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 简单队列，消息生产者
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 13:58
 */
public class Send {
    private static final String QUEUE_NAME="test_simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取一个连接
        Connection connection = ConnectionUtil.getConnection();

        // 从连接中获取一个通道
        Channel channel = connection.createChannel();

        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "hello simple !";

        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("send message");

        channel.close();
        connection.close();
    }
}
