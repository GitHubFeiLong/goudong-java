package com.goudong.oauth2.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.goudong.core.security.aes.AESUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class AppIdUtilTest {

    @Test
    void createAppId() throws UnsupportedEncodingException {
        String appId = AppIdUtil.getAppId(1L);
        System.out.println("appId = " + appId);
    }

    @Test
    void getAppId() {

        String idStr = "1667779450730426368";
        String appId = AppIdUtil.getAppId(Long.parseLong(idStr));
        System.out.println("appId = " + appId);

        Long appId2 = AppIdUtil.getAppId(appId);
        System.out.println("appId2 = " + appId2);

    }
}
