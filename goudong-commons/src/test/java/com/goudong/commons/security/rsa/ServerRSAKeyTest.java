package com.goudong.commons.security.rsa;

import org.junit.jupiter.api.Test;

class ServerRSAKeyTest {

    @Test
    public void publicKey() throws NoSuchFieldException, IllegalAccessException {
        String encrypt = ServerRSAKey.getInstance().publicKeyEncrypt("hello world".getBytes());
        System.out.println("encrypt = " + encrypt);
    }

}