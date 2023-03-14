package com.goudong.wx.central.control.constant;

/**
 * 接口描述：
 * 微信api url 常量
 * @author Administrator
 * @version 1.0
 * @date 2023/3/14 21:05
 */
public interface WxApiUrlConst {
    /**
     * <pre>
     *     获取 Access token接口地址模板
     *     APPID        应用id
     *     APPSECRET    应用密钥
     * </pre>
     */
    String GET_TOKEN_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";
    //~methods
    //==================================================================================================================
}
