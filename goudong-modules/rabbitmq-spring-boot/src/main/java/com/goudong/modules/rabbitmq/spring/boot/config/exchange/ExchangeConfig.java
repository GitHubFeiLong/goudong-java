package com.goudong.modules.rabbitmq.spring.boot.config.exchange;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * 交换机配置
 * @author cfl
 * @version 1.0
 * @date 2022/10/17 10:09
 */
@Configuration
public class ExchangeConfig {

    /**
     * 普通交换机
     */
    public static final String TEST_DIRECT_EXCHANGE = "test.direct.exchange";

    /**
     * 死信交换机
     */
    public static final String TEST_DEAD_LETTER_DIRECT_EXCHANGE = "test.dead.letter.direct.exchange";


    /**
     * 创建一个直连交换机
     * @return
     */
    @Bean
    public DirectExchange testDirectExchange() {
        //  交换机名
        String name = TEST_DIRECT_EXCHANGE;
        // 持久化交换机
        boolean durable = true;
        // autoDelete = false 当没有消费者时，不自动删除交换机
        boolean autoDelete = false;
        // 使用构建者模式创建交换机实例
        ///ExchangeBuilder.directExchange(name).durable(durable).build()
        return new DirectExchange(name, durable, autoDelete);
    }

    /**
     * 创建死信交换机
     * @return
     */
    @Bean
    public Exchange testDeadLetterDirectExchange() {
        return ExchangeBuilder.directExchange(TEST_DEAD_LETTER_DIRECT_EXCHANGE).durable(true).build();
    }
}
