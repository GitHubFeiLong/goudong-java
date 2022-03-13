package com.goudong.commons.framework.openfeign;

import com.goudong.commons.framework.core.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 接口描述：
 * 消息服务
 * @Author e-Feilong.Chen
 * @Date 2021/8/19 8:24
 */
@FeignClient(name = "goudong-message-server", path = "/api/message")
@ResponseBody
public interface GoudongMessageServerService {

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return
     */
    @PostMapping("/code/email-code/{email}")
    Result sendEmailCode (@PathVariable("email") String email);


    /**
     * 发送手机验证码
     * @param phone 手机号码
     * @return
     */
    @PostMapping("/code/phone-code/{phone}")
    Result sendPhoneCode (@PathVariable("phone") String phone);

    /**
     * 验证手机验证码是否正确
     * @param phone 手机号
     * @param code 验证码
     * @return
     */
    @GetMapping("/code/check-phone-code/{phone}/{code}")
    Result<Boolean> checkPhoneCode (@PathVariable String phone, @PathVariable String code);

    /**
     * 验证邮箱验证码是否正确
     * @param email 邮箱
     * @param code 验证码
     * @return
     */
    @GetMapping("/code/check-email-code/{email}/{code}")
    Result<Boolean> checkEmailCode (@PathVariable String email, @PathVariable String code);
}
