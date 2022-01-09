package com.goudong.commons.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 生成6位数字的随机验证码
 * @Author msi
 * @Date 2021-05-07 13:02
 * @Version 1.0
 */
@Deprecated
public class CodeUtil {
    private static final String SYMBOLS = "0123456789";
    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成6位随机数字
     * @return 返回6位数字验证码
     */
    public static String generate() {
        char[] nonceChars = new char[6];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
}

