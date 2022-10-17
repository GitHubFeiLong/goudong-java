package com.goudong.modules.rabbitmq.spring.boot.config.queue;

import com.goudong.modules.rabbitmq.spring.boot.config.binding.BindingConfig;
import com.goudong.modules.rabbitmq.spring.boot.config.exchange.ExchangeConfig;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * 队列配置
 * @author cfl
 * @version 1.0
 * @date 2022/10/17 10:19
 */
@Configuration
public class QueueConfig {

    /**
     * 普通队列
     */
    public static final String TEST_QUEUE = "test.queue";

    /**
     * 死信队列
     */
    public static final String TEST_DEAD_LETTER_QUEUE = "test.dead.letter.queue";

    /**
     * 新增商铺充值记录的直连型队列
     * @return
     */
    @Bean
    public Queue testQueue() {
        // 队列名
        String name = TEST_QUEUE;
        // 开启队列持久化
        boolean durable = true;
        // 关闭独占
        boolean exclusive = false;
        // 关闭自动删除
        boolean autoDelete = false;
        // 队列额外参数
        Map<String, Object> arguments = new HashMap<>();
        // 队列中的所有消息最大有效时间为 60s
        arguments.put("x-message-ttl", 1000 * 60);
        // 死信交换机
        arguments.put("x-dead-letter-exchange", ExchangeConfig.TEST_DEAD_LETTER_DIRECT_EXCHANGE);
        // 死信路由键
        arguments.put("x-dead-letter-routing-key", BindingConfig.TEST_DEAD_LETTER_ROUTING_KEY);

        // 使用构建者模式创建队列
        /// return QueueBuilder.durable(name).ttl(1000 * 60).deadLetterExchange(ExchangeConfig.TEST_DEAD_LETTER_DIRECT_EXCHANGE).deadLetterRoutingKey(BindingConfig.TEST_DEAD_LETTER_ROUTING_KEY).build();

        return new Queue(name,durable, exclusive, autoDelete, arguments);
    }


    /**
     * 创建死信队列
     * @return
     */
    @Bean
    public Queue testDeadLetterQueue() {
        return QueueBuilder.durable(TEST_DEAD_LETTER_QUEUE).build();
    }
}
