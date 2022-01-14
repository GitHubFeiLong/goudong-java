package com.goudong.commons.utils.core;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void regexUrl() {
        boolean b = StringUtil.regexUrl("http://localhost:9998/bindPage.html");
        Assert.isTrue(b, "正则表达式错误");
    }
}