package com.goudong.commons.config;

import com.goudong.commons.frame.whitelist.WhitelistInitialize;
import com.goudong.commons.openfeign.GoudongUserServerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 类描述：
 * 白名单配置类
 * @author msi
 * @version 1.0
 * @date 2022/1/8 17:52
 */
@Configuration
@Import(FeignConfig.class)
public class WhitelistConfig {

    @Bean
    @ConditionalOnMissingBean
    public WhitelistInitialize whitelistInitialize(GoudongUserServerService goudongUserServerService) {
        return new WhitelistInitialize(goudongUserServerService);
    }
}
