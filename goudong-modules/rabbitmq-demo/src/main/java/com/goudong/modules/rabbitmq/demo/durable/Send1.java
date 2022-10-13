package com.goudong.modules.rabbitmq.demo.durable;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 持久化消息 消息生产者
 * @author cfl
 * @version 1.0
 * @date 2022/10/13 21:55
 */
public class Send1 {
    //~fields
    //==================================================================================================================
    public static final String QUEUE_NAME = "test_durable_queue";
    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();
        // 申明队列，关闭持久化（durable=false）
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 50; i++) {
            String message = "hello durable " + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }

        System.out.println("send durable end");

        channel.close();
        connection.close();
    }
}
