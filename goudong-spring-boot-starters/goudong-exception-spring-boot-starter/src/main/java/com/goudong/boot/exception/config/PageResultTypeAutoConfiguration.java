package com.goudong.boot.exception.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * PageResult
 * @author cfl
 * @version 1.0
 * @date 2022/10/26 14:55
 */
@Configuration
public class PageResultTypeAutoConfiguration {

    @ConditionalOnClass(name = {"org.springframework.data.domain.Page"})
    @Bean
    public void JPA() {

    }

}
