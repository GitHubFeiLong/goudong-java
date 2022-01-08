package com.goudong.user.config.commons;

import com.goudong.commons.aop.LoggingAop;
import com.goudong.commons.aop.RepeatAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/12/11 12:04
 */
@Configuration
public class AopConfig {
    /**
     * 日志切面
     * @param environment
     * @return
     */
    @Bean
    public LoggingAop loggingAop(Environment environment) {
        return new LoggingAop(environment);
    }

    /**
     * 防止重复请求
     * @return
     */
    // @Bean
    // @ConditionalOnClass(value = {RedisOperationsUtil.class, AuthorityUserUtil.class})
    public RepeatAop repeatAop() {
        return new RepeatAop();
    }
}
