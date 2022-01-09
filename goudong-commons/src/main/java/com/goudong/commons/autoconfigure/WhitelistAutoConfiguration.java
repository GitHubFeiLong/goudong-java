package com.goudong.commons.autoconfigure;

import com.goudong.commons.config.FeignConfig;
import com.goudong.commons.constant.core.HttpMethodConst;
import com.goudong.commons.frame.whitelist.WhitelistInitialize;
import com.goudong.commons.openfeign.GoudongUserServerService;
import com.goudong.commons.properties.WhitelistProperties;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.commons.utils.core.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
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
     * @param goudongUserServerService openfeign中的用户服务
     * @param whitelistProperties 白名单配置
     * @return
     */
    @Bean
    @DependsOn("whitelistProperties")
    @ConditionalOnMissingBean
    public WhitelistInitialize whitelistInitialize(GoudongUserServerService goudongUserServerService, WhitelistProperties whitelistProperties) {
        LogUtil.debug(log, "启用了白名单");
        return new WhitelistInitialize(goudongUserServerService, whitelistProperties);
    }

    /**
     * 校验下用户在配置文件中白名单的配置
     * @param whitelistProperties 白名单配置
     * @return
     */
    @Bean
    public WhitelistProperties whitelistProperties(WhitelistProperties whitelistProperties){
        /*
            参数校验
         */
        if (whitelistProperties.getEnable()) {
            whitelistProperties.getWhitelists().stream().forEach(p->{
                // 校验模式
                if (StringUtils.isBlank(p.getPattern())) {
                    throw new BeanCreationException(String.format("自定义配置的白名单，模式（%s）无效",
                            p.getMethods()));
                }

                // 校验方法
                boolean valid = ResourceUtil.validMethods(p.getMethods());
                if (!valid) {
                    throw new BeanCreationException(String.format("自定义配置的白名单，请求方法（%s）无效，有效的方法有：%s",
                            p.getMethods(),
                            HttpMethodConst.ALL_HTTP_METHOD));
                }
            });
        }

        return whitelistProperties;
    }
}
