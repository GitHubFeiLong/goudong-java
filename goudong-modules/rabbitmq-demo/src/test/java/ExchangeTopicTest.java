import com.goudong.modules.rabbitmq.demo.util.ConnectionUtil;
import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * topic交换机测试
 * @author cfl
 * @version 1.0
 * @date 2022/10/11 22:39
 */
@ExtendWith({})
public class ExchangeTopicTest {
    //~fields
    //==================================================================================================================
    public static final String EXCHANGE_NAME = "topic_exchange";
    //~methods
    //==================================================================================================================
    @Test
    void testSend() throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String message = "hello topic";

        channel.basicPublish(EXCHANGE_NAME, "a.b.c.d", null, message.getBytes(Charset.defaultCharset()));

        System.out.println("发送消息：" + message);

        channel.close();
        connection.close();
    }

    @Test
    void testRecv1() throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("topic_queue_recv1", false, false, false, null);

        channel.queueBind("topic_queue_recv1", EXCHANGE_NAME, "a.b.c.d");

        channel.basicQos(1);

        channel.basicConsume("topic_queue_recv1", false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println("topic_recv1 message :" + message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

        try {
            Thread.sleep(100000000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRecv2() throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("topic_queue_recv2", false, false, false, null);

        channel.queueBind("topic_queue_recv2", EXCHANGE_NAME, "a.b.#");

        channel.basicQos(1);

        channel.basicConsume("topic_queue_recv2", false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println("topic_recv2 message :" + message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

        try {
            Thread.sleep(100000000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRecv3() throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("topic_queue_recv3", false, false, false, null);

        channel.queueBind("topic_queue_recv3", EXCHANGE_NAME, "a.*.c.*");

        channel.basicQos(1);

        channel.basicConsume("topic_queue_recv3", false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println("topic_recv3 message :" + message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

        try {
            Thread.sleep(100000000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRecv4() throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("topic_queue_recv4", false, false, false, null);

        channel.queueBind("topic_queue_recv4", EXCHANGE_NAME, "#.d");

        channel.basicQos(1);

        channel.basicConsume("topic_queue_recv4", false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println("topic_recv4 message :" + message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

        try {
            Thread.sleep(100000000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
