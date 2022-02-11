package com.goudong.commons.security.rsa;

import com.goudong.commons.exception.security.rsa.RSANotSupportKeySizeException;
import lombok.Getter;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 接口描述：
 * RSA接口
 * @author msi
 * @version 1.0
 * @date 2022/2/10 21:55
 */
public interface RSA {

    //~fields
    //==================================================================================================================
    /**
     * 算法名称
     */
    String ALGORITHM = "RSA";

    //~methods
    //==================================================================================================================
    /**
     * 公钥加密
     * @param base64Encode base64编码字符串，防止乱码
     * @return 加密后Base64编码后的字符串
     */
    String publicKeyEncrypt(String base64Encode);

    /**
     * 私钥解密
     * @param base64Encode 已经加密好的Base64字符串
     * @return 解码后字符串
     */
    String privateKeyDecrypt(String base64Encode);

    /**
     * 生成签名
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    default String sign(String data, PrivateKey privateKey) throws Exception {
        return RSAUtil.sign(data, privateKey);
    }

    /**
     * 验证数据签名
     * @param srcData
     * @param publicKey
     * @param sign
     * @return
     * @throws Exception
     */
    default boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        return RSAUtil.verify(srcData, publicKey, sign);
    }

    /**
     * Key长度枚举
     */
    @Getter
    enum KeySizeEnum{
        RSA1024(1024, 117, 128),
        RSA2048(2048, 245, 256)
        ;
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

        KeySizeEnum(int keySize, int maxEncryptBlock, int maxDecryptBlock) {
            this.keySize = keySize;
            this.maxEncryptBlock = maxEncryptBlock;
            this.maxDecryptBlock = maxDecryptBlock;
        }

        /**
         * 根据keySize获取枚举
         * @param keySize
         * @return
         */
        public static KeySizeEnum getByKeySize(int keySize) {
            for (KeySizeEnum value : KeySizeEnum.values()) {
                if (value.keySize == keySize) {
                    return value;
                }
            }
            throw new RSANotSupportKeySizeException();
        }
    }
}
