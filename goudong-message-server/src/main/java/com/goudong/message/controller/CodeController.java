package com.goudong.message.controller;

import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.message.config.CodeDirectRabbitConfig;
import com.goudong.message.enumerate.RedisKeyProviderEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.util.Objects;

/**
 * 类描述：
 * 邮箱控制器
 * @Author msi
 * @Date 2021-05-05 10:59
 * @Version 1.0
 */
@Api(tags="验证码")
@Validated
@RestController
@RequestMapping("/code")
@RefreshScope
public class CodeController {

    private final RabbitTemplate rabbitTemplate;

    private final RedisTool redisTool;

    public CodeController(RabbitTemplate rabbitTemplate, RedisTool redisTool) {
        this.rabbitTemplate = rabbitTemplate;
        this.redisTool = redisTool;
    }

    /**
     * 发送邮箱验证码
     * @return
     */
    @PostMapping("/email-code/{email}")
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "email", value = "邮箱", required = true)
    @Whitelist("发送邮箱验证码")
    public Result sendEmailCode (@PathVariable("email") @Email(message = "") String email) {
        AssertUtil.isEmail(email, "邮箱格式错误：" + email);
        rabbitTemplate.convertAndSend(CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE, CodeDirectRabbitConfig.EMAIL_CODE_ROUTING_KEY, email);
        return Result.ofSuccess().clientMessage("发送成功");
    }

    /**
     * 发送手机验证码
     * @return
     */
    @PostMapping("/phone-code/{phone}")
    @ApiOperation(value = "发送手机验证码")
    @ApiImplicitParam(name = "phone", value = "手机号码", required = true)
    @Whitelist("发送短信验证码")
    public Result sendPhoneCode (@PathVariable("phone") String phone) {
        AssertUtil.isPhone(phone, "手机号格式错误：" + phone);
        rabbitTemplate.convertAndSend(CodeDirectRabbitConfig.CODE_DIRECT_EXCHANGE, CodeDirectRabbitConfig.PHONE_CODE_ROUTING_KEY, phone);
        return Result.ofSuccess().clientMessage("发送成功");
    }

    /**
     * 验证手机验证码是否正确
     * @param phone 手机号
     * @param code 验证码
     * @return
     */
    @GetMapping("/check-phone-code/{phone}/{code}")
    @ApiOperation(value = "验证验证码是否正确")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号码", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", required = true)
    })
    @Whitelist("验证手机验证码")
    public Result<Boolean> checkPhoneCode (@PathVariable String phone, @PathVariable String code) {
        String redisValue = redisTool.getString(RedisKeyProviderEnum.PHONE_CODE, phone);
        return Result.ofSuccess(Objects.equals(redisValue, code));
    }

    /**
     * 验证邮箱验证码是否正确
     * @param email 邮箱
     * @param code 验证码
     * @return
     */
    @GetMapping("/check-email-code/{email}/{code}")
    @ApiOperation(value = "验证验证码是否正确")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", required = true)
    })
    @Whitelist("验证邮箱验证码")
    public Result<Boolean> checkEmailCode (@PathVariable String email, @PathVariable String code) {
        String redisValue = redisTool.getString(RedisKeyProviderEnum.EMAIL_CODE, email);
        return Result.ofSuccess(Objects.equals(redisValue, code));
    }
}
