package com.goudong.commons.utils.core;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

class RSAUtilTest {

    @Test
    void generateKeyPair() throws Exception {
        KeyPair keyPair = RSAUtil.generateKeyPair();
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();
        byte[] encoded = aPrivate.getEncoded();
    }

    @Test
    void saveKeyForEncodedBase64() throws Exception {
        KeyPair keyPair = RSAUtil.generateKeyPair();
        RSAUtil.saveKeyForEncodedBase64(keyPair.getPublic(), new File("public.txt"));

    }

    @Test
    void getPublicKey() {
    }

    @Test
    void getPrivateKey() {
    }

    @Test
    void encrypt() {
    }

    @Test
    void decrypt() {
    }
}