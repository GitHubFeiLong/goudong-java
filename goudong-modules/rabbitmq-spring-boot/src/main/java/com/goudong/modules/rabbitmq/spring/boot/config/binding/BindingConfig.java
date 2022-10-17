package com.goudong.modules.rabbitmq.spring.boot.config.binding;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 类描述：
 * 队列和交换机配置
 * @author cfl
 * @version 1.0
 * @date 2022/10/17 10:32
 */
@Configuration
public class BindingConfig {

    /**
     * 路由键
     */
    public static final String TEST_ROUTING_KEY = "test.routing.key";

    /**
     * 死信队列和交换机绑定的路由键
     */
    public static final String TEST_DEAD_LETTER_ROUTING_KEY = "test.dead.letter.routing.key";


    @Resource(name = "testDirectExchange")
    private DirectExchange testDirectExchange;

    @Resource(name = "testDeadLetterDirectExchange")
    private DirectExchange testDeadLetterExchange;

    @Resource(name = "testQueue")
    private Queue testQueue;

    @Resource(name = "testDeadLetterQueue")
    private Queue testDeadLetterQueue;

    /**
     * 绑定队列和交换机
     * @return
     */
    @Bean
    public Binding testBinding() {
        return BindingBuilder.bind(testQueue).to(testDirectExchange).with(TEST_ROUTING_KEY);
    }

    /**
     * 绑定死信队列和交换机
     * @return
     */
    @Bean
    public Binding testDeadLetterBinding() {
        return BindingBuilder.bind(testDeadLetterQueue).to(testDeadLetterExchange).with(TEST_DEAD_LETTER_ROUTING_KEY);
    }

}
