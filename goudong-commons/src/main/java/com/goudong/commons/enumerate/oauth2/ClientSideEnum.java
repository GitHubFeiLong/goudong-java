package com.goudong.commons.enumerate.oauth2;

import com.goudong.commons.utils.core.LogUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 枚举描述：
 * 客户端类型，包含浏览器，app，小程序等
 * @author msi
 * @version 1.0
 * @date 2022/1/19 19:51
 */
@Slf4j
@Getter
public enum ClientSideEnum {
    /**
     * 浏览器
     */
    BROWSER,
    /**
     * app
     */
    APP,
    ;
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 根据headerValue获取枚举
     * @param headerValue
     * @return 存在相同的headerValue时返回枚举，不存在时，默认返回 BROWSER
     */
    public static ClientSideEnum getClientSide(String headerValue) {
        if (StringUtils.isNotBlank(headerValue)) {
            ClientSideEnum[] values = ClientSideEnum.values();
            for (int i = 0, length = values.length; i < length; i++) {
                if (values[i].name().equalsIgnoreCase(headerValue)) {
                    return values[i];
                }
            }
        }

        LogUtil.warn(log, "headerValue={}：值不正确,返回默认BROWSER", headerValue);
        return BROWSER;
    }

}
