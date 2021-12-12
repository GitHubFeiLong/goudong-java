package com.goudong.commons.config;

import com.goudong.commons.core.jpa.MyAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 类描述：
 * commons配置Jpa
 * @author msi
 * @version 1.0
 * @date 2021/12/12 12:05
 */
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef="myAuditorAware")
public class JpaConfig {

    @Bean
    public MyAuditorAware myAuditorAware() {
        return new MyAuditorAware();
    }
}
