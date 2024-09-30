package com.goudong.core.security.rsa;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 接口描述：
 * RSA
 * @author msi
 */
public class RSA {

    //~fields
    //==================================================================================================================
    /**
     * 默认1024
     */
    private RSAKeySizeEnum keySizeEnum = RSAKeySizeEnum.RSA1024;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 私钥
     */
    private PrivateKey privateKey;

    /**
     * 公钥Base64字符串
     */
    private String publicKeyBase64;

    /**
     * 私钥Base64字符串
     */
    private String privateKeyBase64;

    //~methods
    //==================================================================================================================

    private RSA() {
    }

    /**
     * 创建一个RSA对象
     * @return rsa
     */
    public static RSA build() {
        return new RSA();
    }

    /**
     * 设置密钥长度
     * @param keySizeEnum 长度
     * @return rsa
     */
    private RSA keySize(RSAKeySizeEnum keySizeEnum) {
        this.keySizeEnum = keySizeEnum;
        return this;
    }

    /**
     * 设置公钥
     * @param publicKey 公钥
     * @return rsa
     */
    private RSA publicKey(PublicKey publicKey){
        this.publicKey = publicKey;
        this.publicKeyBase64 = RSAUtil.key2Base64(publicKey);
        return this;
    }

    /**
     * 设置公钥Base64字符串
     * @param publicKeyBase64   公钥Base64字符串
     * @return rsa
     */
    private RSA publicKeyBase64(String publicKeyBase64){
        this.publicKeyBase64 = publicKeyBase64;
        this.publicKey = (PublicKey) RSAUtil.base642Key(publicKeyBase64, true);
        return this;
    }

    /**
     * 设置私钥
     * @param privateKey    私钥
     * @return  rsa
     */
    private RSA privateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        this.privateKeyBase64 = RSAUtil.key2Base64(privateKey);
        return this;
    }

    /**
     * 设置私钥Base64字符串
     * @param privateKeyBase64  私钥Base64字符串
     * @return rsa
     */
    private RSA privateKeyBase64(String privateKeyBase64) {
        this.privateKeyBase64 = privateKeyBase64;
        this.privateKey = (PrivateKey) RSAUtil.base642Key(privateKeyBase64, false);;
        return this;
    }

    /**
     * 创建默认长度rsa
     * @return  rsa
     */
    private RSA generateKeypair() {
        try {
            KeyPair keyPair = RSAUtil.generateKeypair(this.keySizeEnum);
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
            this.publicKeyBase64 = RSAUtil.key2Base64(publicKey);
            this.privateKeyBase64 = RSAUtil.key2Base64(privateKey);
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建自定义长度rsa
     * @param keySizeEnum   指定密钥长度
     * @return  rsa
     */
    private RSA generateKeypair(RSAKeySizeEnum keySizeEnum) {
        keySize(keySizeEnum);
        generateKeypair();
        return this;
    }

    //~getter
    //==================================================================================================================

    public RSAKeySizeEnum getKeySizeEnum() {
        return keySizeEnum;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public String getPublicKeyBase64() {
        return publicKeyBase64;
    }

    public String getPrivateKeyBase64() {
        return privateKeyBase64;
    }
}
