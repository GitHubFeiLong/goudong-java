package com.goudong.wx.central.control.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * <pre>
     *     将app的信息放入map中
     *     key：appId
     *     value：appSecret
     * </pre>
     */
    private Map<String, String> appMap;

    //~methods
    //==================================================================================================================

    /**
     * 初始化后进行处理
     */
    @PostConstruct
    public void init() {
        initAppMap();
    }

    /**
     * 初始化 appMap
     */
    private void initAppMap() {
        this.appMap = new HashMap<>(1);
        appMap.put(this.testAppId, this.testAppSecret);
    }
}
