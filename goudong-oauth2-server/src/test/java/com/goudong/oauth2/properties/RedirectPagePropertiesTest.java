package com.goudong.oauth2.properties;

import cn.hutool.core.util.IdUtil;
import com.goudong.commons.pojo.Transition;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RedirectPagePropertiesTest {

    @Test
    void getTransitionPageUrl() {
        RedirectPageProperties redirectPageProperties = new RedirectPageProperties();
        Transition transition = Transition.builder()
                .accessToken(IdUtil.simpleUUID())
                .refreshToken(IdUtil.simpleUUID())
                .accessExpires(new Date())
                .refreshExpires(new Date())
                .redirectUrl(redirectPageProperties.getIndexPage())
                .build();
        String transitionPageUrl = redirectPageProperties.getTransitionPageUrl(transition);
        System.out.println("transitionPageUrl = " + transitionPageUrl);
    }
}