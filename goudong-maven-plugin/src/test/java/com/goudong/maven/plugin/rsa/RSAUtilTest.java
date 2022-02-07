package com.goudong.maven.plugin.rsa;


import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.util.Base64;

class RSAUtilTest {

    @Test
    void generateKeyPair() throws Exception {
        KeyPair keyPair = RSAUtil.generateKeyPair();
        System.out.println("私钥：\n" + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        System.out.println("公钥：\n" + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
    }
}