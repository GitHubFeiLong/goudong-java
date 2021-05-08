package com.goudong.message.controller;

import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.RedisValueUtil;
import com.goudong.message.config.CodeDirectRabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Email;

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

    @Resource
    private RedisValueUtil redisValueUtil;

    /**
     * 发送邮箱验证码
     * @return
     */
    @GetMapping("/email-code/{email}")
    public Result sendEmailCode (@PathVariable("email") @Email(message = "请输入正确邮箱格式") String email) {
        rabbitTemplate.convertAndSend(CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE, CodeDirectRabbitConfig.EMAIL_CODE_ROUTING_KEY, email);
        return Result.ofSuccess();
    }

    /**
     * 发送手机验证码
     * @return
     */
    @GetMapping("/phone-code/{phone}")
    public Result sendPhoneCode (@PathVariable("phone") String phone) {
        AssertUtil.isPhone(phone, "手机号格式错误：" + phone);
        rabbitTemplate.convertAndSend(CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE, CodeDirectRabbitConfig.PHONE_CODE_ROUTING_KEY, phone);
        return Result.ofSuccess();
    }

    /**
     * 验证验证码是否正确
     * @param number 账号
     * @param code 验证码
     * @return
     */
    @GetMapping("/check-code/{number}/{code}")
    public Result<Boolean> checkCode (@PathVariable String number, @PathVariable String code) {
        String redisValue = redisValueUtil.getValue(RedisKeyEnum.MESSAGE_AUTH_CODE, number);
        if (redisValue == null || !redisValue.equals(code)) {
            return Result.ofSuccess(false);
        }
        return Result.ofSuccess(true);
    }

}