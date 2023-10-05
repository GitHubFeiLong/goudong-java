package com.goudong.authentication.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@ExtendWith({})
class BaseUserServiceTest {

    @Test
    void testPassword() {
        // String encode = new BCryptPasswordEncoder().encode("123456");
        String encode = new BCryptPasswordEncoder().encode("pLRtRJ3wtlsmsyErCRrycma3GyFJnPuEqUSQNzxuOpRXZL70srFpAillyQX4eI2Qs65oqvwQ3jbGBKnj0yci3x95uvRSgY18r5X");
        System.out.println("encode = " + encode);

        String replace = UUID.randomUUID().toString().replace("-", "");
        System.out.println("replace = " + replace);
    }
}
