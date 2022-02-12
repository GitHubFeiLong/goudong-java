package com.goudong.commons.security.aes;

import com.goudong.commons.enumerate.core.AESKeySizeEnum;
import com.goudong.commons.exception.security.aes.AESException;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * 类描述：
 * AES算法加密解密
 * @author msi
 * @version 1.0
 * @date 2022/2/12 14:52
 */
public class AES {

    //~fields
    //==================================================================================================================
    /**
     * 默认128
     */
    private AESKeySizeEnum keySizeEnum = AESKeySizeEnum.AES128;

    /**
     * 密钥
     */
    private SecretKey secretKey;

    /**
     * 密钥base64字符串
     */
    private String secretKeyBase64;

    //~methods
    //==================================================================================================================

    /**
     * 私有构造器
     */
    private AES(){

    }

    /**
     * 创建一个AES对象
     * @return
     */
    public static AES build() {
        return new AES();
    }

    /**
     * 生成随机的128密钥
     * @return
     */
    public AES generateKeypair() {
        SecretKey secretKey = AESUtil.generateKeypair(this.keySizeEnum);
        this.secretKey = secretKey;
        return this;
    }

    /**
     * 生成随机的指定长度的密钥
     * @param keySizeEnum
     * @return
     */
    public AES generateKeypair(AESKeySizeEnum keySizeEnum) {
        this.secretKey = AESUtil.generateKeypair(keySizeEnum);
        return this;
    }

    /**
     * 设置密钥
     * @param keyBase64 密钥的Base64编码格式字符串
     * @return
     */
    public AES secretKeyBase64(String keyBase64) {
        this.secretKeyBase64 = keyBase64;
        this.secretKey = AESUtil.getSecretKeyByBase64(keyBase64);
        return this;
    }

    /**
     * 设置密钥
     * @param secretKey AES密钥
     * @return
     */
    public AES secretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    /**
     * 加密
     * @param data 加密字符串
     * @return
     */
    public String encrypt(String data) {
        if (this.secretKey == null) {
            throw new AESException("使用AES加密时，secretKey不能为null");
        }
        return AESUtil.encrypt(this.secretKey, data);
    }

    /**
     * 解密
     * @param base64 Base64编码密文后的字符串
     * @return
     */
    public String decrypt(String base64) {
        if (this.secretKey == null) {
            throw new AESException("使用AES解密时，secretKey不能为null");
        }
        return AESUtil.decrypt(this.secretKey, base64);
    }

    public AESKeySizeEnum getKeySizeEnum() {
        return keySizeEnum;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public String getSecretKeyBase64() {
        return secretKeyBase64 != null ? secretKeyBase64 : (secretKey != null ? Base64.getEncoder().encodeToString(secretKey.getEncoded()) : null);
    }
}