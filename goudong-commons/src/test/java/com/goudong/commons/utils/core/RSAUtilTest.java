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
        String data = "IFKRG1jT18vwfeJ7b3xms/ex0rJ10aS5ZXlhCYtQ3LoHj4Pud86tlVo6E90PaA8fctJdGEhTvNRiHlz1Y78lyreNyfKt5R+Px0xNuHVK43LRfZGF1gVQ446M/1hhL/Z3TM4w/p+M5iYXNKepfG9E+y/f22DWnslMJ1Gw2itfuSQ=";
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