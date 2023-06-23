package com.goudong.oauth2.util.app;

import com.goudong.oauth2.exception.AppException;
import org.junit.jupiter.api.Test;

class AppContextTest {

    @Test
    void getAppId() {
        AppContext appContext = new AppContext(new AppIdV1Strategy());
        long appIdNumber = 1667779450730426368L;
        System.out.println(appIdNumber);
        String appId = appContext.getAppId(appIdNumber);
        System.out.println(appId);
        try {
            Long appId1 = appContext.getAppId("1" + appId);
            System.out.println(appId1);
        } catch ( Exception e) {
            System.out.println(e instanceof AppException);
            e.printStackTrace();
        }

    }

    @Test
    void testGetAppId() {
    }

    @Test
    void getAppSecret() {
    }
}