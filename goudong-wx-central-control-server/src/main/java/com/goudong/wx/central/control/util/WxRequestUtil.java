package com.goudong.wx.central.control.util;

import cn.hutool.http.HttpUtil;
import com.goudong.core.util.MessageFormatUtil;

import static com.goudong.wx.central.control.constant.WxApiUrlConst.GET_TOKEN_TEMPLATE;

/**
 * 类描述：
 * 微信请求工具类
 * @author cfl
 * @version 1.0
 * @date 2023/3/14 21:12
 */
public class WxRequestUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 获取AccessToken
     * @param appId
     * @param appSecret
     * @return
     */
    public static String getAccessToken(String appId, String appSecret) {
        String url = MessageFormatUtil.format(GET_TOKEN_TEMPLATE, appId, appSecret);
        String body = HttpUtil.createGet(url).execute().body();
        System.out.println("body = " + body);
        return body;
    }
}
