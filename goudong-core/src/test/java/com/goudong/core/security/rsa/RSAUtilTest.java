package com.goudong.core.security.rsa;

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
        String data = "K+jpVF8eUwknlRlGopF6VOYv7iTc2C2ecQT6HEDl8iOyA+v3S2EdxkDNKBRn0KM+4ui61hjymlb/6SYhpY9bk+x7kCODtu0dOpkkxA1SfTCrBDrInaQJP+0Wjozas2X1bVArTpPpp0tZ24Thslpr4PzzRArugZYC2HgoOBD/iGuyXzIe12HgkJ6qFtBz3gcmxSEpsQwDSG9vL58Asd4NebGLkEyZnHEY7J9NWN0vp56GqOX4nfu3I1f9OjR9CYY/RuEzSNYlkBkvxYbeHJeTMSjSsKGI1+LstYh/XmavbfwqOPiC7xJLcxZROLt/9qoqZMeZ0PbRPOAAA+tBkWjDfycm+WhOiNDZU6apfyMEeRMZRut3oIKIelv1bVwuTAsgx0uDe9MdfeKy/snax+gdEc8qW+IRVCoNoD2yny8xyduRh+n1au4GNhA1KSmn4UktKWjlX9CcrUgH4pTIyK93bQgr+ADqOwHco5YHgIxQPWTw4PgIeDOsPv5K8DJge+LTeu9PLBdlAQp0OS/gSO1Pca3j7Kmq1dwoWpUZ/uCiXCXdT7gfQi860Xx8IRl0Nz3GlnbPDSaHJKxRFsIS4E5/cY0u4auLZmlRO2OOZbBAsEqX1Mvn2eIlK7+r/oZaMS1XAgQSR2ee3cE3hp4mxHngBoifM3mOz+IxtWiG/mQcEDNTDiAGAhVmXQxQLAYO6gfROgaLeOp3jqwVybUdxcH346jiW8OdKjxzevi0xIRWdJwcXg6umo0v9WcFp5H9IML6lDdvunuD0j+atVoGLBHE/j56ZxhKnUc6HSpv+uuAZeJropf4xysFKnzF9akwu8wH5ba5ezFi6Z6CzPzscDG6lSWUrWomf7jxXsyAIr21jlIG7nIsbApqHit70cpLWWnKuMStDQUVvV/Ecu4G3B5NDbtyEc7BOkskFUUumsJWjZDjtL0J+4/neZpftNf5UPamnEqOcZFEYi+fM48kluBBOL3Q2D2AlwxQoD+N+AERbHkFwT6i+jGlNhrj8HGUEjdI";

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