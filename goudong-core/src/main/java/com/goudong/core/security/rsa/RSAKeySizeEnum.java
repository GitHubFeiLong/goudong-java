package com.goudong.core.security.rsa;

/**
 * 枚举描述：
 * RSA 密钥长度枚举
 * @author msi
 * @version 1.0
 */
public enum RSAKeySizeEnum {
    RSA1024(1024, 117, 128),
    RSA2048(2048, 245, 256)
    ;
    //~fields
    //==================================================================================================================
    /**
     * key长度
     */
    private final int keySize;

    /**
     * 最大编码块大小
     */
    private final int maxEncryptBlock;

    /**
     * 最大解码块大小
     */
    private final int maxDecryptBlock;

    //~methods
    //==================================================================================================================
    RSAKeySizeEnum(int keySize, int maxEncryptBlock, int maxDecryptBlock) {
        this.keySize = keySize;
        this.maxEncryptBlock = maxEncryptBlock;
        this.maxDecryptBlock = maxDecryptBlock;
    }

    /**
     * 根据keySize获取枚举
     * @param keySize   长度
     * @return  枚举
     */
    public static RSAKeySizeEnum getByKeySize(int keySize) {
        for (RSAKeySizeEnum value : RSAKeySizeEnum.values()) {
            if (value.keySize == keySize) {
                return value;
            }
        }
        throw new IllegalArgumentException();
    }

    public int getKeySize() {
        return keySize;
    }

    public int getMaxEncryptBlock() {
        return maxEncryptBlock;
    }

    public int getMaxDecryptBlock() {
        return maxDecryptBlock;
    }
}
