package com.goudong.oauth2.util;

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
    }
}