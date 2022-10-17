package com.goudong.modules.rabbitmq.spring.boot.consumer;

import com.goudong.modules.rabbitmq.spring.boot.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class ConsumerTest {
    @Mock
    Logger log;
    @InjectMocks
    Consumer consumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testTestReceiver() throws IOException {
        consumer.testReceiver("messageStr", null, null);
    }
}

// Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
