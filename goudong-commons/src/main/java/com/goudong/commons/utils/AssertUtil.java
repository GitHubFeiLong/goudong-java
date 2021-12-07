package com.goudong.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.File;

/**
 * 扩展 Assert
 * @Author msi
 * @Date 2021-05-07 15:29
 * @Version 1.0
 */
@Slf4j
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

    /**
     * 断言，name是枚举clazz的成员
     * @param name 成员名称
     * @param clazz 枚举class对象
     * @param message 错误信息
     */
    public static void isEnum (String name, Class clazz, String message) {
        Assert.hasLength(name, "枚举成员不能是null");
        try {
            Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言，diskPath是有效的磁盘路径
     * @param diskPath 磁盘路径
     * @param message 错误信息
     */
    public static void isDiskPath (String diskPath, String message) {
        Assert.hasLength(diskPath, "diskPath 无效");
        // 判断是否正确
        File file = new File(diskPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                throw new IllegalArgumentException(message);
            }
        }
    }
}
