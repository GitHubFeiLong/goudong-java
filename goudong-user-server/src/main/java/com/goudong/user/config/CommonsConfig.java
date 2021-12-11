package com.goudong.user.config;

import com.goudong.commons.aop.LoggingAop;
import com.goudong.commons.core.screw.Screw;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/**
 * 类描述：
 * Commons中的配置进行注入
 * @author msi
 * @version 1.0
 * @date 2021/12/11 9:59
 */
@Configuration
public class CommonsConfig {

    /**
     * 日志切面
     * @param environment
     * @return
     */
    @Bean
    public LoggingAop LoggingAop(Environment environment) {
        return new LoggingAop(environment);
    }

    /**
     * 数据库文档生成
     * @param environment
     * @return
     */
    @Bean
    public Screw screw(Environment environment) {
        Screw screw = Screw.getInstance(environment).create();
        return screw;
    }
}
