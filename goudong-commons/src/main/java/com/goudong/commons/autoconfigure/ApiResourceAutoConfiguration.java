package com.goudong.commons.autoconfigure;

import com.goudong.commons.config.FeignConfig;
import com.goudong.commons.framework.apiResource.ApiResourceInitialize;
import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
import com.goudong.commons.properties.ApiResourceProperties;
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
 * 接口资源自动配置
 * @author cfl
 * @date 2022/8/3 0:25
 * @version 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ApiResourceProperties.class)
@Import({FeignConfig.class})
@ConditionalOnProperty(prefix = "commons.api-resource", name = "enable", havingValue = "true", matchIfMissing = false)
public class ApiResourceAutoConfiguration {

    /**
     * api接口资源出来
     * 让whitelistProperties先加载
     * @param goudongOauth2ServerService openfeign中的权限服务
     * @param apiResourceProperties api接口资源配置属性
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ApiResourceInitialize apiResourceInitialize(GoudongOauth2ServerService goudongOauth2ServerService, ApiResourceProperties apiResourceProperties) {
        LogUtil.debug(log, "启用了接口资源自动配置");
        return new ApiResourceInitialize(goudongOauth2ServerService, apiResourceProperties);
    }

}
