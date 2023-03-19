package com.goudong.wx.central.control.dto.resp;

import lombok.Data;

/**
 * 类描述：
 * 微信返回的基类
 * @author cfl
 * @version 1.0
 * @date 2023/3/15 11:05
 */
@Data
public class BaseResp {

    /**
     * 错误码 0 成功，非0失败
     */
    private int errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 是否时成功的响应
     * @return
     */
    public boolean isSuccessful() {
        return errcode == 0;
    }

    /**
     * 是否时失败的响应
     * @return
     */
    public boolean isUnsuccessful() {
        return errcode != 0;
    }
}
