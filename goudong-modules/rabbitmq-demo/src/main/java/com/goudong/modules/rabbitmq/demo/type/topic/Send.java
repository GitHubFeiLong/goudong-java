package com.goudong.modules.rabbitmq.demo.type.topic;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 主题队列，消息生产者
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 15:21
 */
public class Send {
    public static final String EXCHANGE_NAME = "test_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        // 声明交换机 topic：主题模式
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String message = "商品...";

        // goods.delete 只有消费者2号能收到（goods.#）
        channel.basicPublish(EXCHANGE_NAME, "goods.delete", null, message.getBytes());
        // 两个消费者都能收到
        // channel.basicPublish(EXCHANGE_NAME, "goods.add", null, message.getBytes());

        System.out.println("send message = " + message);

        channel.close();
        connection.close();
    }
}
