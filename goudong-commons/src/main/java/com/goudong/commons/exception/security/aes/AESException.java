package com.goudong.commons.exception.security.aes;

import com.goudong.boot.exception.core.BasicException;
import com.goudong.boot.exception.enumerate.ServerExceptionEnum;

/**
 * 类描述：
 * 自定义RSA异常
 * @author msi
 * @version 1.0
 * @date 2022/2/11 21:09
 */
public class AESException extends BasicException {

    public AESException() {
        super(ServerExceptionEnum.SERVER_ERROR, "AES异常");
    }

    public AESException(String serverMessage) {
        super(ServerExceptionEnum.SERVER_ERROR, serverMessage);
    }
}
