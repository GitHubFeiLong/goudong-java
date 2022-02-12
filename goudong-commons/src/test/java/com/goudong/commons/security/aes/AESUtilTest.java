package com.goudong.commons.security.aes;

import org.junit.jupiter.api.Test;

class AESUtilTest {

    @Test
    void generateKeypair() {
        String a = AES.build().secretKeyBase64(null).encrypt("hello world");
        String a1 = AES.build().secretKeyBase64("6dYlsIJCciby9jjKo+ud4XjNXrPs61Vs2UU+M8u6oCk=").decrypt(a);
        System.out.println("a = " + a);
        System.out.println("a1 = " + a1);

    }
}