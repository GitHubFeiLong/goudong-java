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
        // byte[] encrypt = RSAUtil.pubKeyEncrypt("你是傻逼吗草密码是123456".getBytes());
        byte[] encrypt = RSAUtil.pubKeyEncrypt("你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请".getBytes());
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

    @Test
    void priKeyDecrypt () throws Exception {
        String data = "GE7cmNOwx37B+lUdlDVPU2iiPsNouHOy9+wyeoXrBhDekHVTzMSXtLsJf+Bj9kNG+5dQ6QKv+lyBYlCgrCOyAWBjh7rhtkyW1G5ZPA+HvuvTps3rr6mHGKFJxTm0noPXF4+66iuC1KFrQ+rUrZhS2AirwJQXsQJ3KZEH3EKU92mNyvBHFKLrS2w6JwHlaRraCwHS2KBv3zSlp+6bqwBVJW2ofrszLNgwK6PrP1+FImBJKL4HRyifuDsP0V8tx5Zi5jw2C95jOJWjIK+lzbOWHlBQ2KfaxNh20Q7sTloMeX3P213TnkDBhbRNtoLK1IKPNbGgqrYiF7Rc36hqJLQQuA==";
        byte[] bytes = RSAUtil.priKeyDecrypt(data.getBytes());
        System.out.println("new String(bytes) = " + new String(bytes));
    }
}