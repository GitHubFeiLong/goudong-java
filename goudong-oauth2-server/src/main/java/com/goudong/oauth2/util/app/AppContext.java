package com.goudong.oauth2.util.app;

import com.goudong.oauth2.enumerate.ExceptionEnum;
import com.goudong.oauth2.exception.AppException;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/6/22 13:00
 */
public class AppContext {
    //~fields
    //==================================================================================================================
    private AppIdStrategy appIdStrategy;

    public AppContext(AppIdStrategy appIdStrategy) {
        this.appIdStrategy = appIdStrategy;
    }

    public AppContext(String appId) {
        if (appId.startsWith(AppIdV1Strategy.PREFIX)) {
            this.appIdStrategy = new AppIdV1Strategy() ;
        } else {
            throw AppException.builder(ExceptionEnum.X_APP_ID_INVALID).code("400").clientMessage("请求头X-App-Id格式错误").build();
        }
    }
    //~methods
    //==================================================================================================================
    /**
     * 根据Long类型的appId获取对应的String类型的appId
     * @param appId
     * @return
     */
    public String getAppId(Long appId) {
        return appIdStrategy.getAppId(appId);
    };

    /**
     * 根据String类型appId获取对应的Long类型appId
     * @param appId
     * @return
     */
    public Long getAppId(String appId) {
        return appIdStrategy.getAppId(appId);
    }

    /**
     * 生成密钥
     * @return
     */
    public String getAppSecret() {
        return appIdStrategy.getAppSecret();
    }

}
