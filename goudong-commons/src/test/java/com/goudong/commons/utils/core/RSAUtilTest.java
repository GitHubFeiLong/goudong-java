package com.goudong.commons.utils.core;

import cn.hutool.core.codec.Base64;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

class RSAUtilTest {

    @Test
    void getInstance() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAUtil instance = RSAUtil.getInstance();
        System.out.println("instance = " + instance);
    }

    @Test
    void getPubKeyBase64() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String pubKeyBase64 = RSAUtil.getPubKeyBase64();
        System.out.println("pubKeyBase64 = " + pubKeyBase64);
    }

    @Test
    void pubKeyEncrypt() throws Exception {
        // byte[] encrypt = RSAUtil.pubKeyEncrypt("你是傻逼吗草密码是123456".getBytes());
        byte[] encrypt = RSAUtil.pubKeyEncrypt("你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请".getBytes());
        System.out.println("公钥加密后 = " + Base64.encode(encrypt));
        byte[] decrypt = RSAUtil.priKeyDecrypt(encrypt);
        System.out.println("私钥解密后 = " + new String(decrypt));
    }

    @Test
    void priKeyEncrypt() throws Exception {
        byte[] encrypt = RSAUtil.priKeyEncrypt("你是傻逼吗草密码是123456".getBytes("utf-8"));
        System.out.println("私钥加密后 = " + Base64.encode(encrypt));
        byte[] decrypt = RSAUtil.pubKeyDecrypt(encrypt);
        System.out.println("公钥解密后 = " + new String(decrypt,"utf-8"));
    }

    @Test
    void priKeyDecrypt () throws Exception {
        // 前端加密并编码后的字符串
        String data = "IBHp54Wo5zkolk5kX1XEKW0A3bMC1rFRiKCsI1jyAbC7BLwRQGyfFT0YdO6H6WVqxT+/AR3iYtASMkpr1slJGbKTbsc/AR8vp1LIk2vmNEBJ66oZB0Ucrs/lM5mUnOhU4tAI4DgviMW0uXJH/zrucJfG+3WybCEwAj0Vi6706fQ=";
        // 前端加密后的字符串需要使用decode解码。
        byte[] decode = Base64.decode(data);
        byte[] bytes = RSAUtil.priKeyDecrypt(decode);
        System.out.println("new String(bytes) = " + new String(bytes));
    }

    @Test
    void test1() {
        System.out.println();
    }
}