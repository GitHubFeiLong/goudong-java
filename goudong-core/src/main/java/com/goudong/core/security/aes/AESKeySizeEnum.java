package com.goudong.core.security.aes;

/**
 * 枚举描述：
 * AES长度枚举
 * @author msi
 */
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
    private final int keySize;


    //~methods
    //==================================================================================================================
    AESKeySizeEnum(int keySize) {
        this.keySize = keySize;
    }

    /**
     * 根据keySize获取枚举
     * @param keySize   长度
     * @return  枚举对象
     */
    public static AESKeySizeEnum getByKeySize(int keySize) {
        for (AESKeySizeEnum value : AESKeySizeEnum.values()) {
            if (value.keySize == keySize) {
                return value;
            }
        }
        throw new IllegalArgumentException();
    }

    public int getKeySize() {
        return keySize;
    }
}
