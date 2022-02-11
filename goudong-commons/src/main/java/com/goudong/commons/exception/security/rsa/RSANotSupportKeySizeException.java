package com.goudong.commons.exception.security.rsa;

/**
 * 类描述：
 * 系统编码，RSA不支持key长度
 * @author msi
 * @version 1.0
 * @date 2022/2/11 20:32
 */
public class RSANotSupportKeySizeException extends RSAException {

    /**
     * 服务端异常
     */
    public RSANotSupportKeySizeException() {
        super("本项目中，RSA暂时只支持用1024或2048生成的密钥");
    }
    /**
     * 服务端异常
     *
     * @param serverMessage 服务端错误信息
     */
    public RSANotSupportKeySizeException(String serverMessage) {
        super(serverMessage);
    }

}