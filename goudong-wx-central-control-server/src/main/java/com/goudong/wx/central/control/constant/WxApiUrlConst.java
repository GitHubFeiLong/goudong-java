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
     * <p>获取 Access token接口地址模板-GET</p>
     * <p>APPID        应用id</p>
     * <p>APPSECRET    应用密钥</p>
     *
     */
    String GET_TOKEN_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";

    /**
     * 获取稳定版接口调用凭据-POST
     */
    String GET_STABLE_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/stable_token";

    /**
     * 创建菜单接口-POST
     */
    String POST_CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token={ACCESS_TOKEN}";

    /**
     * 设置行业-POST
     */
    String POST_API_SET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token={ACCESS_TOKEN}";

    /**
     * 获取行业-GET
     */
    String GET_INDUSTRY = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token={ACCESS_TOKEN}";

    /**
     * 获得模板ID-GET
     */
    String GET_API_ADD_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token={ACCESS_TOKEN}";

    /**
     * 获取模板列表
     */
    String GET_ALL_PRIVATE_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token={ACCESS_TOKEN}";

    /**
     * 发送模板消息
     */
    String POST_SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={ACCESS_TOKEN}";
    //~methods
    //==================================================================================================================
}
