package com.goudong.commons.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 扩展 Assert
 * @Author msi
 * @Date 2021-05-07 15:29
 * @Version 1.0
 */
public class AssertUtil extends Assert{
    /**
     * 手机号正则表达式
     */
    public static final String PHONE_REGEX = "^1([358][0-9]|4[01456879]|6[2567]|7[0135678]|9[012356789])[0-9]{8}$";

    /**
     * 邮箱正则表达式
     */
    public static final String EMAIL_REGEX =  "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";


    /**
     * 正则表达式验证手机号
     * @param phone 手机号
     * @return
     */
    public static void isPhone(String phone, String message) {
        Assert.hasLength(phone, "正则校验手机号格式，手机号不能为空");
        Assert.isTrue(phone.matches(PHONE_REGEX), message);
    }

    /**
     * 正则表达式验证邮箱
     * @param email 邮箱
     * @return
     */
    public static void isEmail(String email, String message) {
        Assert.hasLength(email, "邮箱不能为空");
        Assert.isTrue(email.matches(EMAIL_REGEX), message);
    }
}
