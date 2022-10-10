package com.goudong.modules.rabbitmq.demo.transaction;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 事务机制，消息生产者
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 17:07
 */
public class Send {

    public static final String QUEUE_NAME = "test_queue_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String msg = "hello tx message";

        try{
            // 开启事务
            channel.txSelect();
            // 发送消息
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            // 手动触发异常
            System.out.println(1/0);
            //提交事务
            channel.txCommit();
        } catch(Exception e){
            // 回滚事务
            channel.txRollback();
            System.out.println("send message txRollback");
        } finally{
            channel.close();
            connection.close();
        }
    }
}
