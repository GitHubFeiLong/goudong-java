package com.goudong.commons.utils.core;

import cn.hutool.core.util.StrUtil;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
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
 * goudong项目的RSA加密、解密工具耦合性较高
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
     * 后端公钥
     */
    private PublicKey serverPublicKey;

    /**
     * 后端私钥
     */
    private PrivateKey serverPrivateKey;

    /**
     * 前端端公钥
     */
    private PublicKey uiPublicKey;
    /**
     * 算法名称
     */
    private static final String ALGORITHM = "RSA";

    /**
     * 算法名称/加密模式/数据填充方式
     * 默认：RSA/ECB/PKCS1Padding
     */
    private static final String ALGORITHMS = "RSA/ECB/PKCS1Padding";

    /**
     * 值根据生成密钥的大小有关(1024:117;2048:245)
     * @see com.sun.crypto.provider.RSACipher#buffer
     */
    public static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * 值根据生成密钥的大小有关(1024:128;2048:256)
     * @see com.sun.crypto.provider.RSACipher#buffer
     */
    public static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 服务端私钥文件保存文件位置
     */
    private static final String SERVER_PRIVATE_KEY_PATH = new StringBuilder()
            .append(".ssh")
            .append(File.separator)
            .append("server_rsa")
            .toString();
    /**
     * 服务端公钥文件保存文件位置
     */
    private static final String SERVER_PUBLIC_KEY_PATH = new StringBuilder()
            .append(".ssh")
            .append(File.separator)
            .append("server_rsa.pub")
            .toString();

    /**
     * ui公钥文件保存文件位置
     */
    private static final String UI_PUBLIC_KEY_PATH = new StringBuilder()
            .append(".ssh")
            .append(File.separator)
            .append("ui_rsa.pub")
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
                    PublicKey serverPublicKey = getServerPublicKey();
                    PrivateKey serverPrivateKey = getServerPrivateKey();
                    PublicKey uiPublicKey = getUiPublicKey();
                    rsaUtil = new RSAUtil(serverPublicKey, serverPrivateKey, uiPublicKey);
                }
            }
        }
        return rsaUtil;
    }

    /**
     * 获取类路径.ssh下的server_rsa.pub文件内容，并转换成公钥对象
     * @return 公钥对象
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getServerPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource publicClassPathResource = new ClassPathResource(SERVER_PUBLIC_KEY_PATH);
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

        String errorMessage = StrUtil.format("后端保存公钥的资源文件不存在:{}", SERVER_PUBLIC_KEY_PATH);
        throw new FileNotFoundException(errorMessage);
    }

    /**
     * 获取类路径.ssh下的server_rsa文件内容，并转换成公钥对象
     * @return 公钥对象
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PrivateKey getServerPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource privateClassPathResource = new ClassPathResource(SERVER_PRIVATE_KEY_PATH);
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

        String errorMessage = StrUtil.format("保存私钥的资源文件不存在:{}", SERVER_PRIVATE_KEY_PATH);
        throw new FileNotFoundException(errorMessage);
    }

    /**
     * 获取类路径.ssh下的ui_rsa.pub文件内容，并转换成公钥对象
     * @return 前端公钥对象
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getUiPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource publicClassPathResource = new ClassPathResource(UI_PUBLIC_KEY_PATH);
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

        String errorMessage = StrUtil.format("前端保存公钥的资源文件不存在:{}", UI_PUBLIC_KEY_PATH);
        throw new FileNotFoundException(errorMessage);
    }
    /**
     * 获取公钥的Base64格式字符串
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String getServerPubKeyBase64() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return Base64.getEncoder().encodeToString(getInstance().serverPublicKey.getEncoded());
    }
    /**
     * 公钥加密数据
     * @param plainData 被加密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] serverPubKeyEncrypt(byte[] plainData) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, getInstance().serverPublicKey);

        return subsectionEncrypt(plainData, cipher);
    }
    
    /**
     * 私钥加密数据
     * @param plainData 被加密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] serverPriKeyEncrypt(byte[] plainData) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（私钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, getInstance().serverPrivateKey);

        // 加密数据, 返回加密后的密文
        return subsectionEncrypt(plainData, cipher);
    }

    /**
     * 分段加密
     * @param plainData 需要加密的数据
     * @param cipher 密码器
     * @return 加密后的数据
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    private static byte[] subsectionEncrypt(byte[] plainData, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, IOException {
        int inputLen = plainData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(plainData, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(plainData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        // 加密数据, 返回加密后的密文
        byte[] encryptedData = out.toByteArray();
        out.close();
        
        return encryptedData;
    }

    /**
     * 公钥解密数据
     * @param cipherData 需要解密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] serverPubKeyDecrypt(byte[] cipherData) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, getInstance().serverPublicKey);

        // 解密数据, 返回解密后的明文
        return subsectionDecrypt(cipherData, cipher);
    }

    /**
     * 私钥解密数据
     * @param cipherData 需要解密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] serverPriKeyDecrypt(byte[] cipherData) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（私钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, getInstance().serverPrivateKey);

        // 解密数据, 返回解密后的明文
        return subsectionDecrypt(cipherData, cipher);
    }

    /**
     * 分段解密
     * @param cipherData 需要解密的数据
     * @param cipher 密码器
     * @return 返回解密后的数据
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    private static byte[] subsectionDecrypt(byte[] cipherData, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, IOException {
        int inputLen = cipherData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(cipherData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(cipherData, offSet, inputLen - offSet);
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
     * 使用前端公钥加密数据
     * @param cipherData 需要解密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] uiPubKeyEncrypt(byte[] cipherData) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥解密模型）
        cipher.init(Cipher.ENCRYPT_MODE, getInstance().uiPublicKey);

        // 解密数据, 返回解密后的明文
        return subsectionEncrypt(cipherData, cipher);
    }

    /**
     * 使用前端公钥解密密数据
     * @param cipherData 需要解密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] uiPubKeyDecrypt(byte[] cipherData) throws Exception {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（私钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, getInstance().uiPublicKey);

        // 解密数据, 返回解密后的明文
        return subsectionDecrypt(cipherData, cipher);
    }


    /**
     * 私有构造方法，使用单例的懒汉，进行双锁创建对象
     * @param serverPublicKey
     * @param serverPrivateKey
     * @param uiPublicKey
     */
    private RSAUtil(PublicKey serverPublicKey, PrivateKey serverPrivateKey, PublicKey uiPublicKey) {
        this.serverPublicKey = serverPublicKey;
        this.serverPrivateKey = serverPrivateKey;
        this.uiPublicKey = uiPublicKey;
    }

}
