package com.goudong.oauth2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.redis.EnableCommonsRedisConfig;
import com.goudong.boot.web.EnableCommonsWebMvcConfig;
import com.goudong.boot.web.aop.ApiLogAop;
import com.goudong.boot.web.bean.DatabaseKey;
import com.goudong.boot.web.bean.DatabaseKeyInterface;
import com.goudong.boot.web.properties.ApiLogProperties;
import com.goudong.commons.annotation.enable.EnableCommonsFeignConfig;
import com.goudong.commons.annotation.enable.EnableCommonsJacksonConfig;
import com.goudong.commons.annotation.enable.EnableCommonsJpaConfig;
import com.goudong.commons.aop.LoggingAop;
import com.goudong.oauth2.enumerate.DatabaseKeyEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：
 * 引用 commons中的配置
 * @author msi
 * @version 1.0
 * @date 2022/1/23 11:20
 */
@Configuration
@EnableCommonsRedisConfig
@EnableCommonsWebMvcConfig
@EnableCommonsFeignConfig
@EnableCommonsJpaConfig
@EnableCommonsJacksonConfig
public class CommonsConfig {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 接口日志切面
     * @param environment
     * @return
     */
    @Bean
    public ApiLogAop ApiLogAop(Environment environment, ObjectMapper objectMapper, ApiLogProperties apiLogProperties) {
        return new ApiLogAop(environment, objectMapper, apiLogProperties);
    }

    /**
     * 方法日志切面
     * @param environment
     * @return
     */
    @Bean
    public LoggingAop loggingAop(Environment environment, ObjectMapper objectMapper) {
        return new LoggingAop(environment, objectMapper);
    }

    /**
     * 数据库索引异常配置
     * @return
     */
    @Bean
    public DatabaseKey databaseKey() {
        Map<String, String> map = Stream.of(DatabaseKeyEnum.values()).collect(Collectors.toMap(DatabaseKeyInterface::getKey, p -> p.getClientMessage(), (k1, k2) -> k1));
        DatabaseKey databaseKey = new DatabaseKey(map);
        return databaseKey;
    }
}
