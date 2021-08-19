package com.goudong.commons.openfeign;

import com.goudong.commons.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Email;

/**
 * 接口描述：
 * 消息服务
 * @Author e-Feilong.Chen
 * @Date 2021/8/19 8:24
 */
@FeignClient(name = "goudong-message-server", path = "/api/message")
@ResponseBody
public interface MessageService {

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return
     */
    @GetMapping("/email-code/{email}")
    Result sendEmailCode (@PathVariable("email") @Email(message = "请输入正确邮箱格式") String email);


    /**
     * 发送手机验证码
     * @param phone 手机号码
     * @return
     */
    @GetMapping("/phone-code/{phone}")
    Result sendPhoneCode (@PathVariable("phone") String phone);


    /**
     * 验证验证码是否正确
     * @param number 手机号码/邮箱
     * @param code 验证码
     * @return
     */
    @GetMapping("/check-code/{number}/{code}")
    Result<Boolean> checkCode (@PathVariable String number, @PathVariable String code);
}
