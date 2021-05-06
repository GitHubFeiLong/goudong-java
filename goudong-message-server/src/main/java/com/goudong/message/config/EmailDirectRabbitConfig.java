package com.goudong.message.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * 类描述：
 * 直连型交换机
 * @Author msi
 * @Date 2021-05-05 10:19
 * @Version 1.0
 */
public class EmailDirectRabbitConfig {

    /**
     * 邮箱验证码 直连型队列
     */
    public static final String EMAIL_CODE_DIRECT_QUEUE = "emailCodeDirectQueue";

    /**
     * 邮箱交换机 直连型交换机
     */
    public static final String EMAIL_DIRECT_EXCHANGE = "emailDirectExchange";

    /**
     * 邮箱验证码队列
     * @return
     */
    @Bean
    public Queue emailCodeDirectQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(EMAIL_CODE_DIRECT_QUEUE,true);
    }

    /**
     * 交换机邮箱
     * @return
     */
    @Bean
    public DirectExchange emailCodeDirectExchange() {
        return new DirectExchange(EMAIL_DIRECT_EXCHANGE,true,false);
    }

    /**
     * 绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
     * @return
     */
    @SuppressWarnings("ALL")
    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(emailCodeDirectQueue()).to(emailCodeDirectExchange()).with("TestDirectRouting");
    }
}
