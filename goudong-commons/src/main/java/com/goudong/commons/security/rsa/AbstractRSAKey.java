package com.goudong.commons.security.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/2/9 20:24
 */
public abstract class AbstractRSAKey {

    public static final String ALGORITHM = "RSA";

    /**
     * 值根据生成密钥的大小有关(1024:117;2048:245)
     */
    public static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * 值根据生成密钥的大小有关(1024:128;2048:256)
     */
    public static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 公钥
     */
    protected PublicKey publicKey;

    /**
     * 私钥
     */
    protected PrivateKey privateKey;

    /**
     * 公钥Base64编码的字符串
     */
    protected String publicKeyBase64;

    /**
     * 私钥Base64编码的字符串
     */
    protected String privateKeyBase64;

    abstract PublicKey getPublicKey();
    abstract PrivateKey getPrivateKey();
    abstract String getPublicKeyBase64();
    abstract String getPrivateKeyBase64();

    /**
     * 公钥加密
     * @param publicKey 公钥
     * @param data 需要加密的数据字节数组
     * @return 返回Base64编码字符串
     * @throws Exception
     */
    public static String publicKeyEncrypt(PublicKey publicKey, byte[] data) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 分段加密
        return sectionEncrypt(data, cipher);
    }

    /**
     * 私钥加密
     * @param privateKey 私钥
     * @param data 需要加密的数据字节数组
     * @return 返回Base64编码字符串
     * @throws Exception
     */
    public static String privateKeyEncrypt(PrivateKey privateKey, byte[] data) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        // 分段加密
        return sectionEncrypt(data, cipher);
    }

    /**
     * 公钥解密
     * @param publicKey 公钥
     * @param base64Encode base64编码后的字符串
     * @return
     * @throws Exception
     */
    public static byte[] publicKeyDecrypt(PublicKey publicKey, String base64Encode) throws Exception {
        byte[] data = Base64.getDecoder().decode(base64Encode);
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        // 分段解密
        return sectionDecrypt(data, cipher);
    }

    /**
     * 私钥解密
     * @param privateKey 私钥
     * @param base64Encode base64编码后的字符串
     * @return
     * @throws Exception
     */
    public static byte[] privateKeyDecrypt(PrivateKey privateKey, String base64Encode) throws Exception {
        byte[] data = Base64.getDecoder().decode(base64Encode);
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // 分段加密
        return sectionDecrypt(data, cipher);
    }

    /**
     * 分段加密
     * @param data 需要加密的数据
     * @param cipher 密码器
     * @return 加密后的数据
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    private static String sectionEncrypt(byte[] data, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, IOException {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        // 加密数据, 返回加密后的密文
        byte[] encryptedData = out.toByteArray();
        out.close();

        // Base64编码
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * 分段解密
     * @param data 需要解密的数据
     * @param cipher 密码器
     * @return 返回解密后的数据
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    private static byte[] sectionDecrypt(byte[] data, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, IOException {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥加密
     * @param data 需要加密的数据字节数组
     * @return 返回Base64编码字符串
     * @throws Exception
     */
    public String publicKeyEncrypt(byte[] data) throws Exception {
        // 分段加密
        return AbstractRSAKey.publicKeyEncrypt(this.getPublicKey(), data);
    }

    /**
     * 私钥加密
     * @param data 需要加密的数据字节数组
     * @return 返回Base64编码字符串
     * @throws Exception
     */
    public String privateKeyEncrypt(byte[] data) throws Exception {
        // 分段加密
        return AbstractRSAKey.privateKeyEncrypt(this.getPrivateKey(), data);
    }

    /**
     * 公钥解密
     * @param base64Encode base64编码后的字符串
     * @return
     * @throws Exception
     */
    public byte[] publicKeyDecrypt(String base64Encode) throws Exception {
        // 分段解密
        return AbstractRSAKey.publicKeyDecrypt(this.getPublicKey(), base64Encode);
    }

    /**
     * 私钥解密
     * @param base64Encode base64编码后的字符串
     * @return
     * @throws Exception
     */
    public byte[] privateKeyDecrypt(String base64Encode) throws Exception {
        // 分段加密
        return AbstractRSAKey.privateKeyDecrypt(this.getPrivateKey(), base64Encode);
    }

}