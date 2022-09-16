package com.goudong.junit5.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceTest {

    @Autowired
    Service service;

    @Test
    void add() {
        service.add(1, 2);
    }
}