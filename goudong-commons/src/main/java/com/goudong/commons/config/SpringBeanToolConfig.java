package com.goudong.commons.config;

import com.goudong.commons.utils.core.SpringBeanTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/3/14 14:50
 */
@Configuration
public class SpringBeanToolConfig {
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Bean
    public SpringBeanTool springBeanTool () {
        return new SpringBeanTool();
    }
}
