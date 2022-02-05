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
        String data = "GAIwAupuUresJN6hLgrXH+ZV+Hl3WkUsO7mPp8MGNkb4KK9pVyZ+IbUxH86a4PBBVgqhqJ2F4tlsrp8f5plYQ7eSh/GfVuYkNdQgdNgrff5vnV1L47gUJWl4oHwfCVGdcu7TTEY3ViqPnv9FsE4/Nlrhch2oOdvbUUnsQ0ojTidYz5Dw5XhH41H01FuHjMJx8iz11I5DIabBgz3aEZjE6gS0sS9md6oV0IK6sZv+hJSEPccO1E01HvsCNMT5GehM78+HUvF5Q4v0y4ajpzyczOYncw1gCI63Iv4cUJRyYnTfMpReshXA9limWm48aTLqlb3H6Qejaf4Zs7nBtk2To1aMiVoQDkyQ+J6VU9CGGHFjl0KT6NThfAiqC2H2eB5daWHmg9wrS6DBMI83ezv8hQC6Xhvki/rlsb5/UKWthvSN4PhALDAnEmF8fvSErNEp7s4mTJ9RpDlIE+BCdNTAXPEFMB/GrPnirj9V2b0Yzaj12U2ZaXcTMNZ/LqcB0bc1bIgFQCBTYcUUz+6c3ujuy0DX2Xqtjn/AC1lJYJ+XEImgfaXPWjjA29dtXMsqqMI4M8tliWyO3W0rDtJZ3st2MFgYURhl6kbwjdyWECV9xbjT6wmreszTPyRv9hYy1BRq8g2BAKrTO28jrAoTpXWaXus2M1ffK0PQZOg3ueDGVY9X8gUTlFgFn9wanMx388JlyDSX3Dsbo9u8OaVpnQp4eWznZMluEPmg6l5G2OFBAlGJxhj1pk4A8BZ9tzoK8MvuoSevcRQgscNpfGEG+JjgCDgOz3OaBZ8/DuHowd1c9h1OIG20v2bBaIyKqODcosIuD8pQVcHokX5HDGo2o2aECwlnKfJtakaDb5vChUsjPiG6HkHIyZZjKXjO5jfPabE2JojORFMfbmqyL2R5OmWKCOrzCGexe1mzZL8NvQ8vfsqkAiJaiBJkkSd/FQY91yGUqlUO5ZJ/9+FGMRZaOQfS+VEB1GHMRmIyyPJDRTOsi/IEWeSSGni9lBgAaQ99qyCJQyiMrFDWZ3Rj3eeIKPReUCSCFwhFOacOPiAr66QQMAmmcn/pLMctRtL2SrARXJD0T2QLr2gi9olyObIaVQune3aEAZBXKkIIbqK57kveWs8s7CRmdnlpe6T3nZawn+tyhLCBaq8sPnoQ93+xRTz3gX4dsh3KA4sPPqoXQyjpTHLJcTl9YCgmjkIZ11mrRStGdYB2T0E1CnBlmhfJ9HHT/n36ZyAKTGk63WA9yDxgSXwKDGTI+hggtIV6gRHazMkNqOLtJ4R2GjD/wsoYBSL6PBrDmotP/inEZKZAqC7cPIdtPxNch78eQDaHwWkMq1WdF1GvGOb/+mb5Xk1vCpu/mW3R1vTNaeBO6/lIdw5NINTKf5xZmwLSoA8rLm6nD9G5IWefmYI+wpgdQ/DvypCxetZOn87M/vkS3VY0pZG6/P3s6yZk8oy/sl/Ukjl0A8ZgC6/Fljg1D69ai+P9Nj/FOpri8cIj4UZnLsaJ7MmEY+jrgMi2EM3/MGFnCMvrwOE=";
        byte[] bytes = RSAUtil.priKeyDecrypt(data.getBytes());
        System.out.println("new String(bytes) = " + new String(bytes));
    }
}