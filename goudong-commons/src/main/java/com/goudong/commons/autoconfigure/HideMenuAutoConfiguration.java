package com.goudong.commons.autoconfigure;

import com.goudong.commons.config.FeignConfig;
import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
import com.goudong.commons.framework.runner.HideMenuRunner;
import com.goudong.commons.properties.HideMenuProperties;
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
 * 隐藏菜单那的自动配置类
 * @author cfl
 * @date 2022/9/19 22:41
 * @version 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(HideMenuProperties.class)
@Import({FeignConfig.class})
@ConditionalOnProperty(prefix = "commons.hide-menu", name = "enable", havingValue = "true", matchIfMissing = false)
public class HideMenuAutoConfiguration {

    /**
     * 隐藏菜单处理
     * @param goudongOauth2ServerService openfeign中的权限服务
     * @param properties 隐藏菜单配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public HideMenuRunner hideMenuRunner(GoudongOauth2ServerService goudongOauth2ServerService, HideMenuProperties properties) {
        LogUtil.debug(log, "启用了隐藏菜单自动配置");
        return new HideMenuRunner(goudongOauth2ServerService, properties);
    }

}
