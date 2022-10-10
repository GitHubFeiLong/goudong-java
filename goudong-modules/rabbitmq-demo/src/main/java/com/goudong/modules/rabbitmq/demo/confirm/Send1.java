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
public class Send1 {

    public static final String QUEUE_NAME = "test_queue_confirm1";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 生产者调用confirmSelect 将chancel设置为confirm模式。注意（事务机制改为这个会出异常）
        channel.confirmSelect();

        String msg = "hello confirm message";

        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        System.out.println("send message txRollback");


        /*
            确认消息
            堵塞线程等待服务器返回响应
            如果服务器确认消费者已经发送完成则返回true
         */
        if(channel.waitForConfirms()){
            System.out.println("message send ok");
        } else {
            System.out.println("message send failed");
        }

        channel.close();
        connection.close();
    }
}
