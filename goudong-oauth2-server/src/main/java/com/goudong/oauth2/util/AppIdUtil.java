package com.goudong.oauth2.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;

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
     * 生成一个 携带{@code PREFIX}开头的Base64格式字符串，并去掉末尾的”=“，全部小写
     * @param appId
     * @return
     */
    public static String getAppId(Long appId) {
        try {
            Assert.notNull(appId);
            byte[] bytes = appId.toString().getBytes("utf-8");
            return PREFIX + Base64.getEncoder().withoutPadding().encodeToString(bytes).toLowerCase();
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
