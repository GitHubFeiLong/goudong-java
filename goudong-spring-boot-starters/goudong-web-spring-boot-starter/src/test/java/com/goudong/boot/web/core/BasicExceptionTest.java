package com.goudong.boot.web.core;

import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({})
class BasicExceptionTest {

    @Test
    void testBuilder() {
        BasicException build = BasicException
                .builder(ServerExceptionEnum.SERVER_ERROR)
                .clientMessage("11111")
                .clientMessageTemplate("{}不存在")
                .clientMessageParams("123")
                .serverMessage("服务器错误了啊")
                .serverMessageTemplate("{} 出现没错误")
                .serverMessageParams("localhost")
                .build();
        System.out.println("build = " + build);
    }
}