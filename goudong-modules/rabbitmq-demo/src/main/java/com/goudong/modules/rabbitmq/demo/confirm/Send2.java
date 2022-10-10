package com.goudong.modules.rabbitmq.demo.confirm;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * confirm 模式，消息生产者
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 17:31
 */
public class Send2 {

    public static final String QUEUE_NAME = "test_queue_confirm2";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 生产者调用confirmSelect 将chancel设置为confirm模式。注意（事务机制改为这个会出异常）
        channel.confirmSelect();

        // 批量发送
        for (int i = 0; i < 10; i++) {
            String msg = "hello confirm message";

            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        }

        System.out.println("send message txRollback");

        // 确认是否发送成功
        if(channel.waitForConfirms()){
            System.out.println("message send ok");
        } else {
            System.out.println("message send failed");
        }

        channel.close();
        connection.close();
    }
}
