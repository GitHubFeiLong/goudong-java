package com.goudong.commons.utils.core;

import cn.hutool.core.util.StrUtil;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 类描述：
 * RSA加密、解密
 * @author msi
 * @date 2022/2/4 18:07
 * @version 1.0
 */
public class RSAUtil {

    /**
     * 单例
     */
    private static RSAUtil rsaUtil;

    /**
     * 公钥
     */
    private PublicKey publicKey;
    /**
     * 私钥
     */
    private PrivateKey privateKey;

    /**
     * 算法名称
     */
    private static final String ALGORITHM = "RSA";

    /**
     * 私钥文件保存文件位置
     */
    private static final String PRIVATE_KEY_PATH = new StringBuilder()
            .append(".ssh")
            .append(File.separator)
            .append("id_rsa")
            .toString();
    /**
     * 公钥文件保存文件位置
     */
    private static final String PUBLIC_KEY_PATH = new StringBuilder()
            .append(".ssh")
            .append(File.separator)
            .append("id_rsa.pub")
            .toString();

    /**
     * 使用加锁双检单例-懒汉式，获取RSAUtil对象
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAUtil getInstance() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (rsaUtil == null) {
            synchronized (RSAUtil.class) {
                if (rsaUtil == null) {
                    PublicKey publicKey = getPublicKey();
                    PrivateKey privateKey = getPrivateKey();
                    rsaUtil = new RSAUtil(publicKey, privateKey);
                }
            }
        }
        return rsaUtil;
    }

    /**
     * 获取类路径.ssh下的id_rsa.pub文件内容，并转换成公钥对象
     * @return 公钥对象
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource publicClassPathResource = new ClassPathResource(PUBLIC_KEY_PATH);
        // 公钥文件是否存在
        if (publicClassPathResource.exists()) {
            File publicKeyFile = publicClassPathResource.getFile();
            // 公钥文件存储时使用Base64编码后保存d的
            String publicKeyBase64 = IOUtil.readFile(publicKeyFile);

            // 把 公钥的Base64文本进行解码
            byte[] encPubKey = Base64.getDecoder().decode(publicKeyBase64);

            // 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
            return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(encPubKey));
        }

        String errorMessage = StrUtil.format("保存公钥的资源文件不存在:{}", PUBLIC_KEY_PATH);
        throw new FileNotFoundException(errorMessage);
    }

    /**
     * 获取类路径.ssh下的id_rsa.pub文件内容，并转换成公钥对象
     * @return 公钥对象
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource privateClassPathResource = new ClassPathResource(PRIVATE_KEY_PATH);
        // 私钥文件是否存在
        if (privateClassPathResource.exists()) {
            File privateKeyFile = privateClassPathResource.getFile();
            // 公钥文件存储时使用Base64编码后保存d的
            String privateKeyBase64 = IOUtil.readFile(privateKeyFile);

            // 把 公钥的Base64文本进行解码
            byte[] encPriKey = Base64.getDecoder().decode(privateKeyBase64);

            // 获取指定算法的密钥工厂, 根据 已编码的私钥规格, 生成私钥对象
            return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(encPriKey));
        }

        String errorMessage = StrUtil.format("保存私钥的资源文件不存在:{}", PRIVATE_KEY_PATH);
        throw new FileNotFoundException(errorMessage);
    }

    /**
     * 获取公钥的Base64格式字符串
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String getPubKeyBase64() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return Base64.getEncoder().encodeToString(getInstance().publicKey.getEncoded());
    }
    /**
     * 公钥加密数据
     * @param plainData 被加密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] pubKeyEncrypt(byte[] plainData) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, getInstance().publicKey);

        // 加密数据, 返回加密后的密文
        return cipher.doFinal(plainData);
    }

    /**
     * 私钥加密数据
     * @param plainData 被加密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] priKeyEncrypt(byte[] plainData) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（私钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, getInstance().privateKey);

        // 加密数据, 返回加密后的密文
        return cipher.doFinal(plainData);
    }

    /**
     * 公钥解密数据
     * @param cipherData 需要解密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] pubKeyDecrypt(byte[] cipherData) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, getInstance().publicKey);

        // 解密数据, 返回解密后的明文
        return cipher.doFinal(cipherData);
    }

    /**
     * 私钥解密数据
     * @param cipherData 需要解密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] priKeyDecrypt(byte[] cipherData) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（私钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, getInstance().privateKey);

        // 解密数据, 返回解密后的明文
        return cipher.doFinal(cipherData);
    }

    /**
     * 私有构造方法，使用单例的懒汉，进行双锁创建对象
     * @param publicKey
     * @param privateKey
     */
    private RSAUtil(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

}
