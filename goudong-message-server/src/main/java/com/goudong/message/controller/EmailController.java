package com.goudong.message.controller;

import com.goudong.commons.pojo.Result;
import com.goudong.message.config.EmailDirectRabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import java.util.UUID;

/**
 * 类描述：
 * 邮箱控制器
 * @Author msi
 * @Date 2021-05-05 10:59
 * @Version 1.0
 */
@Validated
@RestController
@RequestMapping("/api/message/email")
public class EmailController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送验证码
     * @return
     */
    @PostMapping("/email-code")
    public Result sendEmailCode (@Email(message = "请输入正确邮箱格式") String email) {
        rabbitTemplate.convertAndSend(EmailDirectRabbitConfig.EMAIL_DIRECT_EXCHANGE, "TestDirectRouting", UUID.randomUUID()+ email);
        return Result.ofSuccess();
    }
}
