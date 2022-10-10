package com.goudong.modules.rabbitmq.demo.type.work.fair_dispatch;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 工作队列，公平分发，消息生产者
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 14:30
 */
public class Send {

    public static final String QUEUE_NAME = "test_fair_dispatch_work_queue";

    /**
     *                  |--> C2
     * P ---> Queue ----|
     *                  |--> C1
     * @param args
     * @throws IOException
     * @throws TimeoutException
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取channel
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        /**
         * 每个消费者 发送确认消息之前，消息队列不发送下一个消息到消费者，一次只处理一个消息
         * 限制发送给同一个消费者 不得超过一条消息
         */
        channel.basicQos(1);

        // 发送消息
        for (int i = 0; i < 50; i++) {
            String msg= "send hello " + i;
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("【WQ】 send msg = " + msg);
            Thread.sleep(i*5);
        }
        // 关闭资源
        channel.close();
        connection.close();

    }
}
