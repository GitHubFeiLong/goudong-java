package com.goudong.core.security.aes;


import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;

class AESUtilTest {

    @Test
    void test1() {
        // String de = AES.build().secretKeyBase64(Base64.getEncoder().encodeToString("s9f9VYygjxjI03mT".getBytes()))
        //         .decrypt("U2FsdGVkX1+5hgrY1OJ9nvnOQyxXZBl0obLq2u5jha0=");

        SecretKeySpec aes1 = new SecretKeySpec("iIkR7r3Q1WQgC5hG".getBytes(), "AES");
        // String hello_world = AESUtil.encrypt(aes1, "hello world");
        // System.out.println("hello_world = " + hello_world);
        System.out.println("解密 = " + AESUtil.decrypt(aes1, "9rOWsRChv2yROTWcPgDhzQ=="));

        // String aes = AES.build().secretKey(new SecretKeySpec("s9f9VYygjxjI03mT".getBytes(), "AES"))
        //         .decrypt("U2FsdGVkX1+5hgrY1OJ9nvnOQyxXZBl0obLq2u5jha0=");
        //
        // System.out.println("aes = " + aes);
    }
}