package com.goudong.commons.security.rsa;

import cn.hutool.core.util.StrUtil;
import com.goudong.commons.utils.core.IOUtil;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 类描述：
 * 前端服务的RSA
 * @author msi
 * @version 1.0
 * @date 2022/2/9 20:42
 */
public class UiRSAKey extends AbstractRSAKey {

    //~fields
    //==================================================================================================================
    /**
     * 单例
     */
    private static UiRSAKey uiRSAKey;

    /**
     * 公钥文件保存文件位置
     */
    private static final String PUBLIC_KEY_PATH = new StringBuilder().append(".ssh").append(File.separator).append("ui").append(File.separator).append("rsa.pub").toString();

    //~methods
    //==================================================================================================================

    private UiRSAKey(PublicKey publicKey, PrivateKey privateKey, String publicKeyBase64, String privateKeyBase64) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.publicKeyBase64 = publicKeyBase64;
        this.privateKeyBase64 = privateKeyBase64;
    }

    /**
     * 使用加锁双检单例-懒汉式，获取RSAKey对象
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static UiRSAKey getInstance() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (uiRSAKey == null) {
            synchronized (RSAKey.class) {
                if (uiRSAKey == null) {
                    PublicKey publicKey = getPublicKeyByFile();
                    String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                    uiRSAKey = new UiRSAKey(publicKey, null, publicKeyBase64, null);
                }
            }
        }
        return uiRSAKey;
    }

    /**
     * 获取文件内容，并转换成公钥对象
     * @return 公钥对象
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getPublicKeyByFile() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource publicClassPathResource = new ClassPathResource(PUBLIC_KEY_PATH);
        // 公钥文件是否存在
        if (publicClassPathResource.exists()) {
            File publicKeyFile = publicClassPathResource.getFile();
            // 公钥文件存储时使用Base64编码后保存d的
            String publicKeyBase64 = IOUtil.readFile(publicKeyFile);

            // 把 公钥的Base64文本进行解码
            byte[] encPubKey = Base64.getDecoder().decode(publicKeyBase64);

            // 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encPubKey));
        }

        String errorMessage = StrUtil.format("保存公钥的资源文件不存在:{}", PUBLIC_KEY_PATH);
        throw new FileNotFoundException(errorMessage);
    }

    @Override
    PublicKey getPublicKey() {
        return this.publicKey;
    }

    @Override
    PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    @Override
    String getPublicKeyBase64() {
        return this.publicKeyBase64;
    }

    @Override
    String getPrivateKeyBase64() {
        return this.privateKeyBase64;
    }
}