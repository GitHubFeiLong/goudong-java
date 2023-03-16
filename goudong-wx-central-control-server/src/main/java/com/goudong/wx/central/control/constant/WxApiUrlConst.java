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
     *
     * <p>获取 Access token接口地址模板</p>
     * <p>APPID        应用id</p>
     * <p>APPSECRET    应用密钥</p>
     *
     */
    String GET_TOKEN_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";

    /**
     * 获取稳定版接口调用凭据
     */
    String GET_STABLE_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/stable_token";
    //~methods
    //==================================================================================================================
}
