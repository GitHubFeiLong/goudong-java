package com.goudong.commons.properties;

import com.goudong.commons.frame.whitelist.BaseWhitelistDTO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * 类描述：
 * 白名单树形配置
 * @author msi
 * @version 1.0
 * @date 2022/1/9 10:51
 */
@Data
@ConfigurationProperties(prefix = "commons.whitelist")
public class WhitelistProperties {

    //~fields
    //==================================================================================================================
    /**
     * 是否开启白名单
     */
    private Boolean enable = false;

    /**
     * 自定义白名单配置
     */
    @NestedConfigurationProperty
    private List<BaseWhitelistDTO> whitelists;
    //~methods
    //==================================================================================================================

}