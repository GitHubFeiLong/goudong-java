package com.goudong.oauth2.properties;

import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
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
 * @see ClientSideEnum 客户端类型
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
     * 是否允许同时登陆(单个账号允许多设备同时在线)
     * <pre>
     *     情景：员工A在自己电脑使用【admin账号】登录成功后，员工B也在自己的电脑上使用【admin账号】进行登录。
     *     {@code enableRepeatLogin} = true 时：员工A的令牌不受影响，可以正常使用；
     *     {@code enableRepeatLogin} = false 时：员工A的令牌会直接失效。
     * </pre>
     */
    private Boolean enableRepeatLogin = false;

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
     * 是否关闭重复登录
     * @return
     */
    public boolean isDisableRepeatLogin(){
        return !enableRepeatLogin;
    }
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
