package com.goudong.commons.security.rsa;

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

}
