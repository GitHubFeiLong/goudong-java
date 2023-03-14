package com.goudong.wx.central.control.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 类描述：
 * 微信应用配置信息
 * @author cfl
 * @version 1.0
 * @date 2023/3/14 20:25
 */
@Data
@ConfigurationProperties(prefix = "wx.app")
public class WxAppProperties {
    //~fields
    //==================================================================================================================
    /**
     * 测试号 appId
     */
    private String testAppId;

    /**
     * 测试号 app secret
     */
    private String testAppSecret;

    //~methods
    //==================================================================================================================
}
