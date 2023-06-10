package com.goudong.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.redis.EnableCommonsRedisConfig;
import com.goudong.boot.web.EnableCommonsWebMvcConfig;
import com.goudong.boot.web.aop.ApiLogAop;
import com.goudong.boot.web.bean.DatabaseKey;
import com.goudong.boot.web.bean.DatabaseKeyInterface;
import com.goudong.boot.web.properties.ApiLogProperties;
import com.goudong.commons.annotation.enable.*;
import com.goudong.commons.aop.LoggingAop;
import com.goudong.user.enumerate.DatabaseKeyEnum;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
@EnableCommonsWebMvcConfig
@EnableCommonsRedisConfig
@EnableCommonsFeignConfig
@EnableCommonsJpaConfig
@EnableCommonsJacksonConfig
@EnableCommonsUserContextFilter
@EnableCommonsHideMenuConfig
public class CommonsConfig {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 日志切面
     * @param environment
     * @return
     */
    @Bean
    public LoggingAop loggingAop(Environment environment, ObjectMapper objectMapper) {
        return new LoggingAop(environment, objectMapper);
    }

    /**
     * 接口日志切面
     * @param environment
     * @return
     */
    @Bean
    public ApiLogAop apiLogAop(Environment environment, ObjectMapper objectMapper, ApiLogProperties apiLogProperties) {
        return new ApiLogAop(environment, objectMapper, apiLogProperties);
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
