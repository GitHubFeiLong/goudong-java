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

    @Test
    void privateKeyDecrypt1() {
        String data = "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";

        String s1 = ServerRSA.getInstance().privateKeyDecrypt(data);
        System.out.println("s1 = " + s1);
    }

    @Test
    void sign() {
        String data = "hello world 我草拟";
        String sign = ServerRSA.getInstance().sign(data);
        System.out.println("sign = " + sign);
    }

    @Test
    void ver() {
        String data = "hello world";
        String sign = "R34+DDu68jyK/uZVrOT/eFcMfEVAWDtptuxdYMSxnXFxdldvfjTPbyPiDporJUC8BloNTQO3//qY8n16NYNW3q5wQboNKqWFh2ZfCq03pZZfjlQoZzuRYo6e5Eu/ORjFEoRT2mij2jfGC1AHLK2l2I6A3QSsMGa/PGiJv6kj4VttKKu9/u4juVC3p55WUUqkEXcnYjh0UtJsMtWEmDr1e2+XIe3yQnJHCCemIPJudifaLZCZ2Gm9y53M5OGW1OWv9DW2/Kw1sXlxn4qfMRdpwbB8l9/DMgZdaWna4hoWtIjO44VnhUYmWM8wUshBdAexHxu+i3HciW9AHT/Ie16mkA==";
        boolean verify = ServerRSA.getInstance().verify(data, sign);
        System.out.println("verify = " + verify);
    }
}