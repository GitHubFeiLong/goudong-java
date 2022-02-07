package com.goudong.commons.utils.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

class RSAUtilTest {

    @Test
    void getInstance() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAUtil instance = RSAUtil.getInstance();
        System.out.println("instance = " + instance);
    }

    @Test
    void getPubKeyBase64() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String pubKeyBase64 = RSAUtil.getServerPubKeyBase64();
        System.out.println("pubKeyBase64 = " + pubKeyBase64);
    }

    @Test
    void pubKeyEncrypt() throws Exception {
        // byte[] encrypt = RSAUtil.pubKeyEncrypt("你是傻逼吗草密码是123456".getBytes());
        byte[] encrypt = RSAUtil.serverPubKeyEncrypt("你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请你是傻逼吗草密码是12345asdas请".getBytes());
        System.out.println("公钥加密后 = " + Base64.getEncoder().encode(encrypt));
        byte[] decrypt = RSAUtil.serverPriKeyDecrypt(encrypt);
        System.out.println("私钥解密后 = " + new String(decrypt));
    }

    @Test
    void priKeyEncrypt() throws Exception {
        byte[] encrypt = RSAUtil.serverPriKeyEncrypt("你是傻逼吗草密码是123456".getBytes("utf-8"));
        System.out.println("私钥加密后 = " + Base64.getEncoder().encode(encrypt));
        byte[] decrypt = RSAUtil.serverPubKeyDecrypt(encrypt);
        System.out.println("公钥解密后 = " + new String(decrypt,"utf-8"));
    }

    @Test
    void priKeyDecrypt () throws Exception {
        // 前端加密并编码后的字符串
        String data = "egwa40NSUpvuDpIgRZEoEoHiRVn4nF+BQDj8BJZx2dydZ2pbXESzHs+uBkAIoc0JnnluH1yTH95czcHAUe4RGp6eJma6fvY58xbd60YJ8N46Lp+XXNaiqcISfq4876gAjHVjeItkRkc6LzrptDSX7h3LDwQ9ywjR5prDomYsRMuUoTVXiWgAhWSDTIdniXBGrnB++3o07sgGvEbevfM+vWh0r2cYGR4hElwxzGhVY6sdzZUvKeYp4Cc5fPzZlC1+0sw8dUiH0jS/5neeboRKVNO5rV4ALilgLJ7j/WvO75rmW5EA7ULzzbDJeLqv7dxb34cIgl6kuVgcoxSz82hE4WZx+7ufy+0G9AQDHk935bf4jS3PI1CxTP960fykZXrUQmF/0AF4QSHBSay5+Hgh+tqZijIkL8fgN3RFQjEsPUkTmBHqP68vFShBNaxesD95D21hC1MsV8MG50EurRh0qKzLAOvSAcHtNqSFWa0kDt1kkI3m0UDadIcdmcYJ/ujgooinifp/LY3TiK36bmsyr7VVizkitj3PLw/CvCakOm9CUKu5WSdoT5OEfi6eWyj5Lnt+19hcUnNsfeNc+6pcCesGgG0yfjKb9WO8y+538/MgBg+IVLBFCGomncsEq1P7n3hMbdJbFensxHZ9S+wMeihUPvfvg+Q9gKbQ4+U8mxw=";
        // 前端加密后的字符串需要使用decode解码。
        byte[] decode = Base64.getDecoder().decode(data);
        byte[] bytes = RSAUtil.serverPriKeyDecrypt(decode);
        System.out.println("new String(bytes) = " + new String(bytes));
    }

    @Test
    void uiPriKeyDecrypt() throws Exception {
        String data = "RO2xu+JJr1rHBsrAPH6qAGFdn7n47Zw6xB+gzuHYErNqGvzN+f2a0lurmqHV0cuUTU8XyWemwOY1gpEvW0pfjFYclSwVc28TQGDwT+o+bNU2kvUcRO2S41myUPQST7X5V/nwFV8AtjI4iQR95iu6pmEFd551OI14IG0wsGZJImFLjmIml8giYd7uZFiHrzQhHS/RSxN4aI6HstPo9jPkdov87b7RheJhabeS8Apv0Vpw+bWDabL/PqXuL6KyF7xnaj3qGiHYwBjQQEjfGX0aUtuGGQqVGZBjUcVn+pKiK51OyL4r3WVIwko9WZ72e9Fiytq1ku9YI2VYhd8YCRuaUId6OTFCEw418C+CR++k1/2bLuCTSsat7N+GA2HRxWX6oCHvSlVtGmkft2g9NGeLKiEmz9IgywTt1sM7NJaZYmS64em5Y/JpXstLBRz05TesGTsglReTpLBMBIluLSk4UPjve+JZKySfFPmXEOJ0akBhBCsCTqhN9bzI1YTdL6VAHxBxNdZOgqpWSYxo92jRkiLNOX3Mczrdow02vVNSTP4OpOJoP21O44ARkJc/cOY2fCKpuT4hqfBX1btmJ9zEJY7rao/L8/SSGxWaR5nyKEz/+JQh0HnImFy2n8qlz005ynLLLvo8X//fH2ojPtI2aG5UWA+TSjn7lTtKAXHSn8s=";
        // 前端加密后的字符串需要使用decode解码。
        // byte[] decode = Base64.decode(data);
        byte[] decode = Base64.getDecoder().decode(data);
        byte[] bytes = RSAUtil.uiPriKeyDecrypt(decode);
        System.out.println("new String(bytes) = " + new String(bytes));
    }
}