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
public enum AESKeySizeEnum {
    AES128(128),
    AES192(192),
    AES256(256),
    ;
    //~fields
    //==================================================================================================================
    /**
     * key长度
     */
    private int keySize;


    //~methods
    //==================================================================================================================
    AESKeySizeEnum(int keySize) {
        this.keySize = keySize;
    }

    /**
     * 根据keySize获取枚举
     * @param keySize
     * @return
     */
    public static AESKeySizeEnum getByKeySize(int keySize) {
        for (AESKeySizeEnum value : AESKeySizeEnum.values()) {
            if (value.keySize == keySize) {
                return value;
            }
        }
        throw new RSANotSupportKeySizeException();
    }

}
