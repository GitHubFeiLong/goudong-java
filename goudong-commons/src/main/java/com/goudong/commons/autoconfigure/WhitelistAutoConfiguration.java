package com.goudong.commons.autoconfigure;

import com.goudong.commons.config.FeignConfig;
import com.goudong.commons.framework.whitelist.WhitelistInitialize;
import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
import com.goudong.commons.properties.WhitelistProperties;
import com.goudong.commons.utils.core.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 类描述：
 * 白名单自动配置
 * @author msi
 * @date 2022/1/9 11:54
 * @version 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WhitelistProperties.class)
@Import({FeignConfig.class})
@ConditionalOnProperty(prefix = "commons.whitelist", name = "enable", havingValue = "true", matchIfMissing = false)
public class WhitelistAutoConfiguration {

    /**
     * 白名单处理
     * 使用DependsOn，让whitelistProperties先加载
     * @param goudongOauth2ServerService openfeign中的用户服务
     * @param whitelistProperties 白名单配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public WhitelistInitialize whitelistInitialize(GoudongOauth2ServerService goudongOauth2ServerService, WhitelistProperties whitelistProperties) {
        LogUtil.debug(log, "启用了白名单");
        return new WhitelistInitialize(goudongOauth2ServerService, whitelistProperties);
    }

}
