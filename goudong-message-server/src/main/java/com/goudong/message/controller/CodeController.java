package com.goudong.message.controller;

import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.message.config.CodeDirectRabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 类描述：
 * 邮箱控制器
 * @Author msi
 * @Date 2021-05-05 10:59
 * @Version 1.0
 */
@Validated
@RestController
@RequestMapping("/api/message/code")
public class CodeController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送邮箱验证码
     * @return
     */
    @GetMapping("/email-code")
    public Result sendEmailCode (@NotBlank(message = "邮箱不能为空") @Email(message = "请输入正确邮箱格式") String email) {
        rabbitTemplate.convertAndSend(CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE, CodeDirectRabbitConfig.EMAIL_CODE_ROUTING_KEY, email);
        return Result.ofSuccess();
    }

    /**
     * 发送手机验证码
     * @return
     */
    @GetMapping("/phone-code")
    public Result sendPhoneCode (String phone) {
        AssertUtil.isPhone(phone, "手机号格式错误：" + phone);
        rabbitTemplate.convertAndSend(CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE, CodeDirectRabbitConfig.PHONE_CODE_ROUTING_KEY, phone);
        return Result.ofSuccess();
    }
}
