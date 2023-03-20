package com.goudong.boot.web.core;

import org.junit.jupiter.api.Test;

class ClientExceptionTest {

    @Test
    void testBuilder() {
        ClientException build = ClientException.builder()
                .clientMessage("hello")
                .build();
        System.out.println("build = " + build);
    }
}
