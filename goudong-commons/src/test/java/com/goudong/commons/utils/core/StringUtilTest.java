package com.goudong.commons.utils.core;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Objects;

class StringUtilTest {

    @Test
    void replacePathVariable2Asterisk(){
        String s = StringUtil.replacePathVariable2Asterisk("/api/user/{id}");
        Assert.isTrue(Objects.equals(s, "/api/user/*"));

        s = StringUtil.replacePathVariable2Asterisk("/api/user/{id}/{}}");
        Assert.isTrue(Objects.equals(s, "/api/user/*/*}"));


    }
    @Test
    void regexUrl() {
        boolean b = StringUtil.regexUrl("http://localhost:9998/bindPage.html");
        Assert.isTrue(b, "url 正则表达式错误");
    }

    @Test
    void regexUri() {
        Assert.isTrue(StringUtil.regexUri("/api/user/1"), "uri 正则表达式错误");
    }
}