package com.goudong.message.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 系统启动就先加载热数据到redis中
 * @Author msi
 * @Date 2021-04-09 10:24
 * @Version 1.0
 */
@Slf4j
@Component
public class ApplicationRunnerConfig implements ApplicationRunner {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    /**
     * 初始化RabbitMQ【不需要】
     */
    private void initRabbitMQ(){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);

        //创建交换机,并用rabbitAdmin进行声明
        DirectExchange directExchange = new DirectExchange(CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE);
        rabbitAdmin.declareExchange(directExchange);
        //创建队列,设置消息的持久化存储为true
        Queue queue = new Queue(CodeDirectRabbitConfig.EMAIL_CODE_DIRECT_QUEUE, true);
        //创建此队列与交换机的绑定关系,路由键为文章作者的id
        Binding bind = BindingBuilder.bind(queue).to(directExchange).with(CodeDirectRabbitConfig.EMAIL_CODE_ROUTING_KEY);

        //声明队列的使用
        rabbitAdmin.declareQueue(queue);
        //增加队列与交换机之间此作者id的路由键绑定
        rabbitAdmin.declareBinding(bind);
    }
}
