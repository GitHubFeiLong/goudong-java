package com.goudong.commons.openfeign.impl;

import com.goudong.commons.openfeign.MessageService;
import com.goudong.commons.frame.core.Result;

/**
 * 类描述：
 * 消息服务的熔断降级限流异常
 * @Author e-Feilong.Chen
 * @Date 2021/8/19 8:25
 */
public class MessageServiceImpl implements MessageService {

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return
     */
    @Override
    public Result sendEmailCode(String email) {
        return null;
    }

    /**
     * 发送手机验证码
     *
     * @param phone 手机号码
     * @return
     */
    @Override
    public Result sendPhoneCode(String phone) {
        return null;
    }

    /**
     * 验证验证码是否正确
     *
     * @param number 手机号码/邮箱
     * @param code   验证码
     * @return
     */
    @Override
    public Result<Boolean> checkCode(String number, String code) {
        return null;
    }
}
