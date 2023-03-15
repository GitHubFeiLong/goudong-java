package com.goudong.wx.central.control.dto;

import lombok.Data;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/3/15 14:09
 */
@Data
public class AccessTokenDTO {
    /**
     * 获取到的凭证
     */
    private String accessToken;

    /**
     * 到期时间，单位：毫秒，expiresTime 超时5min仍然有效
     */
    private Long expiresTime;
}
