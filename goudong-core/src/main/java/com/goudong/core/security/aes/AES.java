package com.goudong.core.security.aes;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
    public AES generateKeypair(){
        try {
            SecretKey secretKey = AESUtil.generateKeypair(this.keySizeEnum);
            this.secretKey = secretKey;
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成随机的指定长度的密钥
     * @param keySizeEnum
     * @return
     */
    public AES generateKeypair(AESKeySizeEnum keySizeEnum){
        try {
            this.secretKey = AESUtil.generateKeypair(keySizeEnum);
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置密钥
     * @param key 密钥字符串
     * @return
     */
    public AES secretKey(String key) {
        this.secretKey = new SecretKeySpec(key.getBytes(), AESUtil.ALGORITHM);
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
            throw new RuntimeException("使用AES加密时，secretKey不能为null");
        }
        try {
            return AESUtil.encrypt(this.secretKey, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     * @param base64 Base64编码密文后的字符串
     * @return
     */
    public String decrypt(String base64) {
        if (this.secretKey == null) {
            throw new RuntimeException("使用AES解密时，secretKey不能为null");
        }
        return AESUtil.decrypt(this.secretKey, base64);
    }

    public AESKeySizeEnum getKeySizeEnum() {
        return keySizeEnum;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}