package com.goudong.commons.exception.security.rsa;

/**
 * 类描述：
 * 自定义RSA异常
 * @author msi
 * @version 1.0
 * @date 2022/2/11 21:09
 */
public class RSAException extends RuntimeException {

    public RSAException() {
        super("RSA异常");
    }

    public RSAException(String serverMessage) {
        super(serverMessage);
    }
}
