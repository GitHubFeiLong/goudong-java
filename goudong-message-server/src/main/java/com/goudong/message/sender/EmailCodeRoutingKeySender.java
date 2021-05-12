package com.goudong.message.sender;

import com.goudong.commons.utils.AssertUtil;
import com.goudong.message.config.CodeDirectRabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义邮箱发送验证码，ack
 * @Author msi
 * @Date 2021-05-12 10:43
 * @Version 1.0
 */
@Slf4j
@Component
public class EmailCodeRoutingKeySender implements RabbitTemplate.ReturnCallback{

    public static final String LOG_TEMPLATE = "交换机 {} - 发送路由键 {} 内容为:{}";
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * Returned message callback.
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("returnedMessage message:{}, replyCode:{}, replyText:{}, exchange:{}, routingKey:{}", message, replyCode, replyText, exchange, routingKey);
    }

    public void send(String email) {
        rabbitTemplate.setMandatory(true);
        AssertUtil.isEmail(email, "发送验证码，邮箱格式错误");
        log.info(LOG_TEMPLATE, CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE, CodeDirectRabbitConfig.EMAIL_CODE_ROUTING_KEY, email);

        this.rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.error(LOG_TEMPLATE + "发送失败", CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE, CodeDirectRabbitConfig.EMAIL_CODE_ROUTING_KEY, email);
            } else {
                log.info(LOG_TEMPLATE + "发送成功", CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE, CodeDirectRabbitConfig.EMAIL_CODE_ROUTING_KEY, email);
            }
        });
        // 发送
        rabbitTemplate.convertAndSend(CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE, CodeDirectRabbitConfig.EMAIL_CODE_ROUTING_KEY, email);
    }
}
