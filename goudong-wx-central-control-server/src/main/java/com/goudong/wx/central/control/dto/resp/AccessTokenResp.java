package com.goudong.wx.central.control.dto.resp;

import lombok.Data;

/**
 * 类描述：
 * 获取凭证响应对象
 * @author cfl
 * @version 1.0
 * @date 2023/3/15 10:00
 */
@Data
public class AccessTokenResp extends BaseResp{

    /**
     * 获取到的凭证
     */
    private String accessToken;

    /**
     * 凭证有效时间，单位：秒
     */
    private Integer expiresIn;

    /**
     * 到期时间，单位：毫秒
     */
    private Long expiresTime;

    /**
     * 处理 expiresIn和expiresTime
     */
    public void disposeExpires() {
        // 设置到期时间,时间提前5分钟。（注意单位）
        this.setExpiresIn(this.getExpiresIn() - 300);
        this.setExpiresTime(System.currentTimeMillis() + this.getExpiresIn() * 1000);
    }
}
