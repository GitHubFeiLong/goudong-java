package com.goudong.commons.enumerate.core;

import com.goudong.commons.exception.security.rsa.RSANotSupportKeySizeException;
import lombok.Getter;

/**
 * 枚举描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/2/12 13:09
 */
@Getter
public enum RSAKeySizeEnum {
    RSA1024(1024, 117, 128),
    RSA2048(2048, 245, 256)
    ;
    //~fields
    //==================================================================================================================
    /**
     * key长度
     */
    private int keySize;

    /**
     * 最大编码块大小
     */
    private int maxEncryptBlock;

    /**
     * 最大解码块大小
     */
    private int maxDecryptBlock;

    //~methods
    //==================================================================================================================
    RSAKeySizeEnum(int keySize, int maxEncryptBlock, int maxDecryptBlock) {
        this.keySize = keySize;
        this.maxEncryptBlock = maxEncryptBlock;
        this.maxDecryptBlock = maxDecryptBlock;
    }

    /**
     * 根据keySize获取枚举
     * @param keySize
     * @return
     */
    public static RSAKeySizeEnum getByKeySize(int keySize) {
        for (RSAKeySizeEnum value : RSAKeySizeEnum.values()) {
            if (value.keySize == keySize) {
                return value;
            }
        }
        throw new RSANotSupportKeySizeException();
    }

}
