package com.goudong.oauth2.util.app;

import cn.hutool.core.lang.UUID;

/**
 * 接口描述：
 *
 * @author Administrator
 * @version 1.0
 * @date 2023/6/22 12:52
 */
public interface AppIdStrategy {
    //~methods
    //==================================================================================================================
    /**
     * 根据Long类型的appId获取对应的String类型的appId
     * @param appId
     * @return
     */
    String getAppId(Long appId);

    /**
     * 根据String类型appId获取对应的Long类型appId
     * @param appId
     * @return
     */
    Long getAppId(String appId);

    /**
     * 生成密钥
     * @return
     */
    default String getAppSecret() {
        return UUID.randomUUID(true).toString(true);
    }
}
