package com.goudong.commons.security.aes;

import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;

class AESUtilTest {

    @Test
    void generateKeypair() {
        String a = AES.build().secretKeyBase64(null).encrypt("hello world");
        String a1 = AES.build().secretKeyBase64("6dYlsIJCciby9jjKo+ud4XjNXrPs61Vs2UU+M8u6oCk=").decrypt(a);
        System.out.println("a = " + a);
        System.out.println("a1 = " + a1);

    }

    @Test
    void test1() {
        // String de = AES.build().secretKeyBase64(Base64.getEncoder().encodeToString("s9f9VYygjxjI03mT".getBytes()))
        //         .decrypt("U2FsdGVkX1+5hgrY1OJ9nvnOQyxXZBl0obLq2u5jha0=");

        SecretKeySpec aes1 = new SecretKeySpec("s9f9VYygjxjI03mT".getBytes(), "AES");
        String hello_world = AESUtil.encrypt(aes1, "hello world");
        System.out.println("hello_world = " + hello_world);
        System.out.println("解密 = " + AESUtil.decrypt(aes1, hello_world));

        // String aes = AES.build().secretKey(new SecretKeySpec("s9f9VYygjxjI03mT".getBytes(), "AES"))
        //         .decrypt("U2FsdGVkX1+5hgrY1OJ9nvnOQyxXZBl0obLq2u5jha0=");
        //
        // System.out.println("aes = " + aes);
    }
}