package com.goudong.commons.exception;

import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.core.ServerExceptionEnum;
import com.goudong.commons.utils.core.ArrayUtil;
import org.junit.jupiter.api.Test;

class BasicExceptionTest {

    @Test
    void test1() {
        ClientExceptionEnum.BAD_REQUEST.client();
        ClientExceptionEnum.BAD_REQUEST.client("clientMessage");
        ClientExceptionEnum.BAD_REQUEST.client("clientMessage", "serverMessage");
        ClientExceptionEnum.BAD_REQUEST.client("clientMessage:{},{2}", ArrayUtil.create(1, 2));
        ClientExceptionEnum.BAD_REQUEST.client("clientMessage:{},{2}", ArrayUtil.create(1, 2), "serverMessage");
        ClientExceptionEnum.BAD_REQUEST.client("clientMessage", "serverMessage:{},{2}", ArrayUtil.create(1, 2));
        ClientExceptionEnum.BAD_REQUEST.client("clientMessage:{},{2}", ArrayUtil.create(1, 2), "serverMessage:{},{2}", ArrayUtil.create(1, 2));
    }

    @Test
    void test2() {
        // ServerExceptionEnum.SERVER_ERROR.server();
        // ServerExceptionEnum.SERVER_ERROR.server("serverMessage");
        // ServerExceptionEnum.SERVER_ERROR.server("clientMessage", "serverMessage");
        // ServerExceptionEnum.SERVER_ERROR.server("serverMessage:{},{2}", ArrayUtil.create(1, 2));
        // ServerExceptionEnum.SERVER_ERROR.server("clientMessage","serverMessage:{},{2}", ArrayUtil.create(1, 2));
        ServerExceptionEnum.SERVER_ERROR.server("clientMessage:{},{2}", ArrayUtil.create(1, 2), "serverMessage:{},{2}", ArrayUtil.create(1, 2));
    }
}