package com.goudong.oauth2.util.app;

import com.goudong.boot.web.core.ClientException;
import com.goudong.core.util.AssertUtil;
import com.goudong.oauth2.enumerate.ExceptionEnum;
import com.goudong.oauth2.exception.AppException;

import java.util.Base64;

/**
 * 接口描述：
 * AppId v1 版本：使用base64格式
 * @author Administrator
 * @version 1.0
 * @date 2023/6/22 12:52
 */
public class AppIdV1Strategy implements AppIdStrategy{
    /**
     * gd(前缀)$1(版本)$(分隔符)a(次版本)
     * 注意：只能6位
     */
     static final String PREFIX = "gd$1$a";

    public AppIdV1Strategy() {

    }

    /**
     * 根据Long类型的appId获取对应的String类型的appId
     *
     * @param appId
     * @return
     */
    @Override
    public String getAppId(Long appId) {
        try {
            byte[] bytes = appId.toString().getBytes("utf-8");
            return PREFIX + Base64.getEncoder().withoutPadding().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据String类型appId获取对应的Long类型appId
     *
     * @param appId
     * @return
     */
    @Override
    public Long getAppId(String appId) {
        AssertUtil.isTrue(appId.startsWith(PREFIX), () -> AppException.builder(ExceptionEnum.X_APP_ID_INVALID).clientMessage("应用不存在").serverMessage("请勿伪造应用id").build());
        try {
            byte[] bytes = appId.substring((PREFIX).length()).getBytes("utf-8");
            return Long.parseLong(new String(Base64.getDecoder().decode(bytes), "utf-8"));
        } catch (NumberFormatException e) {
            throw ClientException.client("appId格式错误");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //~methods
    //==================================================================================================================

}
