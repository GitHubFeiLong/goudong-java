package com.goudong.user.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-08-13 23:46
 * @Version 1.0
 */
@Slf4j
//@Configuration
public class ApplicationListenerConfig implements ApplicationListener {
    static int count = 0;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        log.info("count : {}", count++);
    }
}
