package com.goudong.modules.rabbitmq.demo.confirm;

import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * confirm 模式，消息生产者
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 17:31
 */
public class Send3 {

    public static final String QUEUE_NAME = "test_queue_confirm3";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false,false,false, null);

        // 生产者调用confirmSelect 将channel设置为confirm模式
        channel.confirmSelect();

        // 存放未确认的消息标识
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

        // 监听通道
        channel.addConfirmListener(new ConfirmListener(){

            // 没问题的handleAck
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if(multiple){
                    System.out.println("【handleAck】-----multiple true");
                    confirmSet.headSet(deliveryTag+1).clear();
                } else {
                    System.out.println("【handleAck】-----multiple false");
                    confirmSet.remove(deliveryTag);
                }
            }

            // handleNack 有问题的
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {

                if(multiple){
                    System.out.println("【N】handleNack-----multiple");
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("【N】handleNack-----multiple false");
                    confirmSet.remove(deliveryTag);
                }
            }
        });

        String msgStr = "ssss";

        while(true){
            long seqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("", QUEUE_NAME, null, msgStr.getBytes());
            confirmSet.add(seqNo);
        }
    }
}
