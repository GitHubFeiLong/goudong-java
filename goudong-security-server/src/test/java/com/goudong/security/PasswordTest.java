package com.goudong.security;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2021/8/19 13:28
 */
public class PasswordTest {
    public static void main(String[] args) {
        String knife4j = BCrypt.hashpw("admin", BCrypt.gensalt());
        System.out.println("knife4j = " + knife4j);
    }
}
