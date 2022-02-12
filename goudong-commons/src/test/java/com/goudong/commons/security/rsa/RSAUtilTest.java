package com.goudong.commons.security.rsa;

import com.goudong.commons.enumerate.core.RSAKeySizeEnum;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

class RSAUtilTest {

    @Test
    void generateKeypair() {
        KeyPair keyPair = RSAUtil.generateKeypair(RSAKeySizeEnum.RSA2048);
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();
        System.out.println("aPublic = " + Base64.getEncoder().encodeToString(aPublic.getEncoded()));
        System.out.println("aPrivate = " + Base64.getEncoder().encodeToString(aPrivate.getEncoded()));
    }

    @Test
    void privateKeyDecrypt() {
        String s = ServerRSA.getInstance().publicKeyEncrypt("hello world");

        System.out.println("s = " + s);

        String s1 = ServerRSA.getInstance().privateKeyDecrypt(s);
        System.out.println("s1 = " + s1);

        String s2 = ServerRSA.getInstance().privateKeyDecrypt("QFHNBLO5klePmDd8F06FYY+8ABhrZwBPOUjJUp1f80AXIpfG3scnCihbXy6I3+ELrcW6jsv/EUY4/d9ePSKCJxfOdFVg95ck/A1mqWqy+uWnLYUbWgYQS6ad5vqiTKfgum7EYE7IbdmGJRRwnU5dTRrBz0tTrDxApZqnKPjb498V6HhXJHVJ2CGh5Njyjj3Ue7fs+nC1aDjZK8DdMSUinrFY1ng8RgSXPOKs96fM+LCrGYX0JlnwE3J7nJS06LMF6ukJ3ghwyLdM/cOBFPy70OYkhXPeyAWe1l/LJpI413GGniUdv8WwvBkQFr5l356wYRuZUn2nFB6iuGYcqZEfGg==");
        System.out.println("s2 = " + s2);
    }
}