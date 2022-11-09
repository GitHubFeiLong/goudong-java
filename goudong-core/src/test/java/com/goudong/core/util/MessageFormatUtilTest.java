package com.goudong.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageFormatUtilTest {

    @Test
    void format() {
        String cfl = MessageFormatUtil.format("hello, 我的姓名是：{}，我今年{}岁了，我喜欢的是{喜欢的事情+%}", "cfl", "7", "网球，篮球");
        assertEquals(cfl, "hello, 我的姓名是：cfl，我今年7岁了，我喜欢的是网球，篮球");


        assertEquals(MessageFormatUtil.format("{}-{}-{}", "1", "2", "3"),
                "1-2-3");
        assertEquals(MessageFormatUtil.format("{}{}{}", "1", "2", "3"),
                "123");

        assertEquals(MessageFormatUtil.format("{一}{二}{三}", "1", "2", "3"),
                "123");

        assertEquals(MessageFormatUtil.format("{一}{二|，\n}{三}}", "1", "2", "3"),
                "123}");
    }

    @Test
    void testFormat() {
        assertEquals(MessageFormatUtil.format(MessageFormatUtil.MessageFormatEnum.PLACEHOLDER_FORMAT,"${}-${}-${}", "1", "2", "3"),
                "1-2-3");
        assertEquals(MessageFormatUtil.format(MessageFormatUtil.MessageFormatEnum.PLACEHOLDER_FORMAT,"${}${}${}", "1", "2", "3"),
                "123");

        assertEquals(MessageFormatUtil.format(MessageFormatUtil.MessageFormatEnum.PLACEHOLDER_FORMAT,"${一}${二}${三}", "1", "2", "3"),
                "123");

        assertEquals(MessageFormatUtil.format(MessageFormatUtil.MessageFormatEnum.PLACEHOLDER_FORMAT,"${一}${二|，\n}${三}}", "1", "2", "3"),
                "123}");
    }

    @Test
    void testFormat1() {
    }
}
