package com.goudong.modules.rabbitmq.spring.boot.producer;

import com.goudong.modules.rabbitmq.spring.boot.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.*;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class TestProducerTest {
    @SpyBean
    RabbitTemplate rabbitTemplate;
    @InjectMocks
    TestProducer testProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testTestSend() {
        testProducer.testSend();
    }
}

// Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
