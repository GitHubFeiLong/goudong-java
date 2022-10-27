package com.goudong.commons.exception.security.aes;

/**
 * 类描述：
 * 自定义RSA异常
 * @author msi
 * @version 1.0
 * @date 2022/2/11 21:09
 */
public class AESException extends RuntimeException {

    public AESException() {
        super("AES异常");
    }

    public AESException(String serverMessage) {
        super(serverMessage);
    }
}
