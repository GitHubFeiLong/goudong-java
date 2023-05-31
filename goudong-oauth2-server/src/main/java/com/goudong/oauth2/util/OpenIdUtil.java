package com.goudong.oauth2.util;

import cn.hutool.core.lang.Assert;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * 类描述：
 * 快登id工具
 * @author cfl
 * @version 1.0
 * @date 2023/5/31 9:12
 */
public class OpenIdUtil {

    /**
     * 生成一个extUserId
     * @param appId
     * @param userId
     * @return
     */
    public static String create(Object appId, Object userId) {
        Assert.notNull(appId);
        Assert.notNull(userId);
        try {
            String appIdStr = appId.toString();
            String userIdStr = userId.toString();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((appIdStr + userIdStr).getBytes("utf-8"));
            String s = Base64.getEncoder().encodeToString(md5.digest());

            if(s.indexOf("=") != -1) {
                s = s.substring(0, s.indexOf("="));
            }
            // 重置
            md5.reset();
            md5.update(appIdStr.getBytes("utf-8"));
            StringBuilder sb = new StringBuilder("o");

            return sb.append(Math.abs(appIdStr.hashCode()) % 10)
                    .append(Base64.getEncoder().encodeToString(md5.digest()).substring(0, 4))
                    .append(s).toString();
        } catch (Exception e) {
            throw new RuntimeException("生成extUserId错误：" + e.getMessage());
        }

    }
}
