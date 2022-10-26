package com.goudong.commons.exception.security.rsa;


import com.goudong.boot.exception.core.BasicException;
import com.goudong.boot.exception.enumerate.ServerExceptionEnum;

/**
 * 类描述：
 * 自定义RSA异常
 * @author msi
 * @version 1.0
 * @date 2022/2/11 21:09
 */
public class RSAException extends BasicException {

    public RSAException() {
        super(ServerExceptionEnum.SERVER_ERROR, "RSA异常");
    }

    public RSAException(String serverMessage) {
        super(ServerExceptionEnum.SERVER_ERROR, serverMessage);
    }
}
