package com.goudong.wx.central.control.constant;

import com.goudong.core.util.MessageFormatUtil;

/**
 * 接口描述：
 * 锁key
 * @author cfl
 * @version 1.0
 * @date 2023/3/15 15:49
 */
public interface LockKeyConst {

    /**
     * 获取token的分布式锁
     */
    String LOCK_GET_ACCESS_TOKEN = "goudong-wx-central-control-server:lock:access-token:${appId}";

    /**
     * 替换占位符位指定参数
     * @param keyTemplate key模板
     * @param params    替换模板的占位符参数
     * @return
     */
    static String getKey(String keyTemplate, Object... params) {
        return MessageFormatUtil.format(MessageFormatUtil.MessageFormatEnum.PLACEHOLDER_FORMAT, keyTemplate, params);
    }
}
