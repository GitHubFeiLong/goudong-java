package com.goudong.modules.rabbitmq.demo.type.routing;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 路由模式队列，消息生产者
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 15:12
 */
public class Send {
    public static final String EXCHANGE_NAME = "test_exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        // exchange direct:直连
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String msg = "hello direct !";
        // routing key
        String routingKey = "info";
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());

        System.out.println("send :" + msg);

        channel.close();
        connection.close();
    }
}
