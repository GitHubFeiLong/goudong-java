package com.goudong.wx.central.control.dto.req;

import lombok.Data;

/**
 * 类描述：
 * 获取稳定版接口调用凭据的接口响应
 * @author cfl
 * @version 1.0
 * @date 2023/3/16 9:14
 */
@Data
public class GetStableAccessTokenReq {
    /**
     * 填写 client_credential
     */
    private String grantType;

    /**
     * 账号唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得。（需要已经成为开发者，且帐号没有异常状态）
     */
    private String appid;

    /**
     * 帐号唯一凭证密钥，即 AppSecret，获取方式同 appid
     */
    private String secret;

    /**
     * 默认使用 false。<br>
     * 1. force_refresh = false 时为普通调用模式，access_token 有效期内重复调用该接口不会更新 access_token；<br>
     * 2. 当force_refresh = true 时为强制刷新模式，会导致上次获取的 access_token 失效，并返回新的 access_token
     */
    private Boolean forceRefresh;

    public GetStableAccessTokenReq(String appid, String secret) {
        this.grantType = "client_credential";
        this.appid = appid;
        this.secret = secret;
        this.forceRefresh = false;
    }

    public GetStableAccessTokenReq(String appid, String secret, Boolean forceRefresh) {
        this.grantType = "client_credential";
        this.appid = appid;
        this.secret = secret;
        this.forceRefresh = forceRefresh;
    }
}
