package com.goudong.commons.utils.core;

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
        byte[] encrypt = RSAUtil.pubKeyEncrypt("你是傻逼吗草密码是123456".getBytes());
        System.out.println("公钥加密后 = " + new String(encrypt));
        byte[] decrypt = RSAUtil.priKeyDecrypt(encrypt);
        System.out.println("私钥解密后 = " + new String(decrypt));
    }

    @Test
    void priKeyEncrypt() throws Exception {
        byte[] encrypt = RSAUtil.priKeyEncrypt("你是傻逼吗草密码是123456".getBytes());
        System.out.println("私钥加密后 = " + new String(encrypt));
        byte[] decrypt = RSAUtil.pubKeyDecrypt(encrypt);
        System.out.println("公钥解密后 = " + new String(decrypt));
    }

}