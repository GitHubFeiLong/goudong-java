package com.goudong.oauth2.properties;

import com.goudong.oauth2.core.TokenExpires;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenExpiresPropertiesTest {

    @Test
    void test1(){
        TokenExpiresProperties tokenExpiresProperties = new TokenExpiresProperties();
        TokenExpires app = tokenExpiresProperties.getApp();
        long l = app.getAccessTimeUnit().toSeconds(app.getAccess());
        System.out.println("l = " + l);
    }
}