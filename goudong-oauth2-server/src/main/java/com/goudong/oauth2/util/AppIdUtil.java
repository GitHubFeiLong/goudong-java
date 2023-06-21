package com.goudong.oauth2.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.core.ServerException;

import javax.websocket.ClientEndpoint;
import java.util.Base64;

/**
 * 类描述：
 * AppId工具
 * @author cfl
 * @version 1.0
 * @date 2023/5/31 9:12
 */
public class AppIdUtil {

    private static final String PREFIX = "gd";

    /**
     * 生成一个 携带{@code PREFIX}开头的Base64格式字符串
     * @param appId
     * @return
     */
    public static String getAppId(Long appId) {
        try {
            Assert.notNull(appId);
            byte[] bytes = appId.toString().getBytes("utf-8");
            // return PREFIX + Base64.getEncoder().withoutPadding().encodeToString(bytes).toLowerCase();
            return PREFIX + Base64.getEncoder().withoutPadding().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取appId对应的Number类型的appId
     * @param appId
     * @return
     */
    public static Long getAppId(String appId) {
        try {
            Assert.notNull(appId);
            byte[] bytes = appId.substring(2).getBytes("utf-8");
            return Long.parseLong(new String(Base64.getDecoder().decode(bytes), "utf-8"));
        } catch (NumberFormatException e) {
            throw ClientException.client("应用id错误");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 生成密钥
     * @return
     */
    public static String getAppSecret() {
        return UUID.randomUUID(true).toString(true);
    }
}
