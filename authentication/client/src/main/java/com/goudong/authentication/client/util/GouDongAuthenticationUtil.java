package com.goudong.authentication.client.util;

import com.goudong.authentication.client.core.CommonConst;

import java.security.PrivateKey;
import java.util.Date;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 */
public class GouDongAuthenticationUtil {
    //~fields
    //==================================================================================================================
    /**
     *
     */
    private Long appId;

    /**
     *
     */
    private String serialNumber;

    /**
     *
     */
    private long timestamp;

    /**
     *
     */
    private String nonceStr;

    /**
     *
     */
    private String body;

    /**
     * 签名
     */
    private String signature;

    //~methods
    //==================================================================================================================

    /**
     *
     * @param appId 应用id
     * @param serialNumber 16位16进制的证书序列号
     * @param body 请求体参数
     * @return
     */
    public static String getAuthentication(Long appId, String serialNumber, String body, PrivateKey privateKey) {
        // 参数校验
        assert appId != null;
        assert serialNumber != null && serialNumber.length() > 0;
        assert body != null;
        assert privateKey != null;

        // 时间戳1970-01-01 00:00:00 至今毫秒数
        long timestamp = new Date().getTime();
        // 12位随机字符
        String nonceStr = GouDongUtil.randomStr(12);

        // 拼装消息
        String message = appId + "\n" + serialNumber + "\n" + timestamp + "\n" + nonceStr + "\n" + body + "\n";
        // 将消息生成签名
        String signature = GouDongUtil.sign(message, privateKey);
        // 生成令牌
        return String.format(CommonConst.AUTHENTICATION_TEMPLATE, appId, serialNumber, timestamp, nonceStr, signature);
    }
}
