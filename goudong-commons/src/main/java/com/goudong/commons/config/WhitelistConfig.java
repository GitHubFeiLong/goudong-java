package com.goudong.commons.config;

import com.goudong.commons.frame.redis.RedisOperations;
import com.goudong.commons.frame.whitelist.WhitelistDao;
import com.goudong.commons.frame.whitelist.WhitelistInitialize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 类描述：
 * 白名单配置类
 * @author msi
 * @version 1.0
 * @date 2022/1/8 17:52
 */
@Configuration
public class WhitelistConfig {

    @Bean
    public WhitelistDao whitelistDao(JdbcTemplate jdbcTemplate) {
        return new WhitelistDao(jdbcTemplate);
    }

    @Bean
    public WhitelistInitialize whitelistInitialize(WhitelistDao whitelistDao, RedisOperations redisOperations) {
        return new WhitelistInitialize(whitelistDao, redisOperations);
    }
}
