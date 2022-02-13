package com.goudong.commons.security.aes;

import com.goudong.commons.enumerate.core.AESKeySizeEnum;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 类描述：
 * AES加密工具类,AES不指定模式和填充，默认为 ECB/PKCS5Padding
 * ECB，CBC，CFB，OFB和CTR
 * 在除ECB以外的所有加密方式中，都需要用到IV对加密结果进行随机化。在使用同一种加密同一个密钥时不应该使用相同的IV
 * 填充：NoPadding, PKCS#5, PKCS#7, ISO 10126, ANSI X9.23和ZerosPadding （PKCS#5, PKCS#7 没太大区别）
 * @author msi
 * @version 1.0
 * @date 2022/2/12 12:55
 */
public class AESUtil {

    //~fields
    //==================================================================================================================
    /**
     * 算法名称
     */
    public static final String ALGORITHM = "AES";

    public static final String CHARACTER_ENCODING = "utf-8";

    //~methods
    //==================================================================================================================


    /**
     * 生成随机的AES密钥
     * @param keySizeEnum
     * @return
     */
    @SneakyThrows
    public static SecretKey generateKeypair(AESKeySizeEnum keySizeEnum) {
        KeyGenerator instance = KeyGenerator.getInstance(ALGORITHM);
        instance.init(keySizeEnum.getKeySize());
        return instance.generateKey();
    }

    /**
     * 生成随机的AES密钥
     * @param keySizeEnum
     * @return
     */
    @SneakyThrows
    public static String generateKeypairBase64(AESKeySizeEnum keySizeEnum) {
        return Base64.getEncoder().encodeToString(generateKeypair(keySizeEnum).getEncoded());
    }

    /**
     * 根据密钥Base64编码字符串获取密钥对象
     * @param keyBase64
     * @return
     */
    public static SecretKey getSecretKeyByBase64(String keyBase64) {
        //5.根据字节数组生成AES密钥
        return new SecretKeySpec(Base64.getDecoder().decode(keyBase64), ALGORITHM);
    }

    /**
     * 使用key进行加密
     * @param secretKey 密钥
     * @param data 待加密的字符串
     * @return 加密后的密文再进行Base64编码后的字符串
     */
    @SneakyThrows
    public static String encrypt(SecretKey secretKey, String data) {
        // 密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte [] bytes = data.getBytes(CHARACTER_ENCODING);
        // 将数据加密,并使用Base64编码。
        return Base64.getEncoder().encodeToString(cipher.doFinal(bytes));
    }

    /**
     * 使用key进行加密
     * @param keyBase64 密钥的Base64编码
     * @param data 待加密的字符串
     * @return 加密后的密文再进行Base64编码后的字符串
     */
    @SneakyThrows
    public static String encrypt(String keyBase64, String data) {
        SecretKey secretKey = getSecretKeyByBase64(keyBase64);
        // 密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte [] bytes = data.getBytes(CHARACTER_ENCODING);
        // 将数据加密,并使用Base64编码。
        return Base64.getEncoder().encodeToString(cipher.doFinal(bytes));
    }

    /**
     * 使用key进行加密
     * @param secretKey 密钥
     * @param base64 使用Base64编码后的密文
     * @return 原始字符串
     */
    @SneakyThrows
    public static String decrypt(SecretKey secretKey, String base64) {
        byte[] data = Base64.getDecoder().decode(base64);
        // 密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // 将数据加密,并使用Base64编码。
        return new String(cipher.doFinal(data));
    }

}