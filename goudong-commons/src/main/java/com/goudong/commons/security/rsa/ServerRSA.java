package com.goudong.commons.security.rsa;

import cn.hutool.core.util.StrUtil;
import com.goudong.commons.enumerate.core.RSAKeySizeEnum;
import com.goudong.commons.utils.core.IOUtil;
import lombok.Data;
import lombok.SneakyThrows;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * 类描述：
 * 后端服务的RSA
 * @author msi
 * @version 1.0
 * @date 2022/2/9 20:42
 */
@Data
public class ServerRSA {
    //~fields
    //==================================================================================================================
    /**
     * 单例
     */
    private static ServerRSA serverRSAKey;

    /**
     * 公钥文件保存文件位置
     */
    @Deprecated
    private static final String PUBLIC_KEY_PATH = new StringBuilder().append(".ssh").append(File.separator).append("server").append(File.separator).append("rsa.pub").toString();

    /**
     * 私钥文件保存文件位置
     */
    @Deprecated
    private static final String PRIVATE_KEY_PATH = new StringBuilder().append(".ssh").append(File.separator).append("server").append(File.separator).append("rsa").toString();

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

    /**
     * 生成密钥的key长度(1024 or 2048)
     */
    private int keySize;

    //~methods
    //==================================================================================================================

    private ServerRSA(PublicKey publicKey, PrivateKey privateKey, String publicKeyBase64, String privateKeyBase64, int keySize) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.publicKeyBase64 = publicKeyBase64;
        this.privateKeyBase64 = privateKeyBase64;
        this.keySize = keySize;
    }

    /**
     * 使用加锁双检单例-懒汉式，获取RSAKey对象
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    @SneakyThrows
    public static ServerRSA getInstance() {
        if (serverRSAKey == null) {
            synchronized (RSAKey.class) {
                if (serverRSAKey == null) {
                    PublicKey publicKey = getPublicKeyByFile();
                    PrivateKey privateKey = getPrivateKeyByFile();
                    String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                    String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

                    // 通过反射获取生成RSA的key长度
                    Field n = RSAPublicKeyImpl.class.getDeclaredField("n");
                    n.setAccessible(true);
                    BigInteger bigInteger = (BigInteger)n.get(publicKey);

                    serverRSAKey = new ServerRSA(publicKey, privateKey, publicKeyBase64, privateKeyBase64, bigInteger.bitLength());
                }
            }
        }
        return serverRSAKey;
    }

    /**
     * 获取文件内容，并转换成公钥对象
     * @return 公钥对象
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    @SneakyThrows
    private static PublicKey getPublicKeyByFile() {
        //ClassPathResource publicClassPathResource = new ClassPathResource(PUBLIC_KEY_PATH);
        File publicKeyFile = new File(ServerRSA.class.getResource("/.ssh/server/rsa").getFile());

        // 公钥文件是否存在
        if (publicKeyFile.exists()) {
            // 公钥文件存储时使用Base64编码后保存d的
            String publicKeyBase64 = IOUtil.readFile(publicKeyFile);

            return (PublicKey) RSAUtil.base642Key(publicKeyBase64, true);
        }

        String errorMessage = StrUtil.format("保存公钥的资源文件不存在:{}", PUBLIC_KEY_PATH);
        throw new FileNotFoundException(errorMessage);
    }

    /**
     * 获取文件内容，并转换成私钥对象
     * @return 私钥对象
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    @SneakyThrows
    private static PrivateKey getPrivateKeyByFile()  {
        File privateKeyFile = new File(ServerRSA.class.getResource("/.ssh/server/rsa").getFile());
        // 私钥文件是否存在
        if (privateKeyFile.exists()) {
            // 公钥文件存储时使用Base64编码后保存d的
            String privateKeyBase64 = IOUtil.readFile(privateKeyFile);

            return (PrivateKey) RSAUtil.base642Key(privateKeyBase64, false);
        }

        String errorMessage = StrUtil.format("保存私钥的资源文件不存在:{}", PRIVATE_KEY_PATH);
        throw new FileNotFoundException(errorMessage);
    }

    /**
     * 公钥加密
     *
     * @param base64Encode      加密的字节数组
     * @return 加密后Base64编码后的字符串
     */
    public String publicKeyEncrypt(String base64Encode) {
        RSAKeySizeEnum byKeySize = RSAKeySizeEnum.getByKeySize(keySize);
        return RSAUtil.publicKeyEncrypt(byKeySize, publicKey, base64Encode);
    }

    /**
     * 私钥解密
     *
     * @param base64Encode 已经加密好的Base64字符串
     * @return 解码后的字符数组
     */
    public String privateKeyDecrypt(String base64Encode) {
        RSAKeySizeEnum byKeySize = RSAKeySizeEnum.getByKeySize(keySize);
        return RSAUtil.privateKeyDecrypt(byKeySize, privateKey, base64Encode);

    }

    /**
     * 生成签名
     *
     * @param data 数据
     * @return 根据data生成签名
     */
    public String sign(String data) {
        return RSAUtil.sign(data, privateKey);
    }

    /**
     * 验证数据签名
     *
     * @param srcData 数据
     * @param sign 签名
     * @return 是否匹配
     */
    public boolean verify(String srcData, String sign) {
        return RSAUtil.verify(srcData, publicKey, sign);
    }
}