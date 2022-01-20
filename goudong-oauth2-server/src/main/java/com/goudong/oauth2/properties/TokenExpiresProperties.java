package com.goudong.oauth2.properties;

import com.goudong.commons.exception.core.ApplicationBootFailedException;
import com.goudong.oauth2.core.TokenExpires;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 令牌失效时常配置
 * @author msi
 * @version 1.0
 * @date 2022/1/19 19:57
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2.token-expires")
public class TokenExpiresProperties {

    //~fields
    //==================================================================================================================
    /**
     * 浏览器配置令牌失效
     */
    @NestedConfigurationProperty
    private TokenExpires browser = new TokenExpires(2, TimeUnit.HOURS);

    /**
     * app配置令牌失效
     */
    @NestedConfigurationProperty
    private TokenExpires app = new TokenExpires(4, TimeUnit.HOURS);

    //~methods
    //==================================================================================================================

    /**
     * 检查
     */
    @PostConstruct
    public void init() {
        checkBrowser();
        checkApp();
    }

    /**
     * 检查 browser配置
     */
    private void checkBrowser() {
        if (this.browser.getAccess() < 1) {
            throw new ApplicationBootFailedException("启动失败", "browser的令牌失效时长配置错误", "oauth2.token-expires.browser.access属性的值需要大于1");
        }

        if (this.browser.getRefresh() < 1) {
            throw new ApplicationBootFailedException("启动失败", "browser的令牌失效时长配置错误", "oauth2.token-expires.browser.refresh属性的值需要大于1");
        }
    }

    /**
     * 检查 app的配置
     */
    private void checkApp() {
        if (this.app.getAccess() < 1) {
            throw new ApplicationBootFailedException("启动失败", "app的令牌失效时长配置错误", "oauth2.token-expires.browser.access属性的值需要大于1");
        }

        if (this.app.getRefresh() < 1) {
            throw new ApplicationBootFailedException("启动失败", "app的令牌失效时长配置错误", "oauth2.token-expires.browser.refresh属性的值需要大于1");
        }
    }
}