package com.goudong.commons.security.rsa;

import com.goudong.commons.enumerate.core.RSAKeySizeEnum;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.util.Base64;

/**
 * * *padding   RSA加密常用的填充方式有下面3种：
 * <p>
 * 1.RSA_PKCS1_PADDING 填充模式，最常用的模式
 * 要求:
 * 输入：必须 比 RSA 钥模长(modulus) 短至少11个字节, 也就是　RSA_size(rsa) – 11如果输入的明文过长，必须切割，　然后填充
 * 输出：和modulus一样长
 * 根据这个要求，对于512bit的密钥，　block length = 512/8 – 11 = 53 字节
 * <p>
 * 2.RSA_PKCS1_OAEP_PADDING
 * 输入：RSA_size(rsa) – 41
 * 输出：和modulus一样长
 * <p>
 * 3.RSA_NO_PADDING  不填充
 * 输入：可以和RSA钥模长一样长，如果输入的明文过长，必须切割，　然后填充
 * 输出：和modulus一样长
 * 跟DES，AES一样，　RSA也是一个块加密算法（ block cipher algorithm），总是在一个固定长度的块上进行操作。但跟AES等不同的是，　block length是跟key length有关的。
 * 每次RSA加密的明文的长度是受RSA填充模式限制的，但是RSA每次加密的块长度就是key length。
 * <p>
 * 需要注意：
 * 假如你选择的秘钥长度为1024bit共128个byte：
 * 1.当你在客户端选择RSA_NO_PADDING填充模式时，如果你的明文不够128字节加密的时候会在你的明文前面，前向的填充零。解密后的明文也会包括前面填充的零，这是服务器需要注意把解密后的字段前向填充的
 * 零去掉，才是真正之前加密的明文。
 * 2.当你选择RSA_PKCS1_PADDING填充模式时，如果你的明文不够128字节加密的时候会在你的明文中随机填充一些数据，所以会导致对同样的明文每次加密后的结果都不一样。对加密后的密文，服务器使用相同的填充方式都能解密。
 * 解密后的明文也就是之前加密的明文。
 * 3.RSA_PKCS1_OAEP_PADDING填充模式没有使用过， 他是PKCS#1推出的新的填充方式，安全性是最高的，和前面RSA_PKCS1_PADDING的区别就是加密前的编码方式不一样。
 * 类描述：
 * RSA工具，静态方法
 * @author msi
 * @version 1.0
 * @date 2022/2/11 19:59
 */
public class RSAUtil {

    //~fields
    //==================================================================================================================
    /**
     * 算法名称
     */
    public static final String ALGORITHM = "RSA";
    /**
     * 签名名称
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    //~methods
    //==================================================================================================================

    /**
     * 生成指定长度的密钥
     * @param keySizeEnum
     * @return
     */
    @SneakyThrows
    public static KeyPair generateKeypair(RSAKeySizeEnum keySizeEnum) {
        // 获取指定算法的密钥对生成器
        KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM);
        // 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
        gen.initialize(keySizeEnum.getKeySize(), new SecureRandom());
        // 随机生成一对密钥（包含公钥和私钥）
        KeyPair keyPair = gen.generateKeyPair();
        return keyPair;
    }

    /**
     * 公钥加密
     *
     * @param publicKey 公钥
     * @param encryptStr 需要加密的字符串
     * @return 双层Base64编码字符串
     */
    @SneakyThrows
    public static String publicKeyEncrypt(RSAKeySizeEnum keySizeEnum, PublicKey publicKey, String encryptStr) {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 分段加密
        return RSAUtil.sectionEncrypt(encryptStr, cipher, keySizeEnum.getMaxEncryptBlock());
    }

    /**
     * 私钥解密
     *
     * @param privateKey   私钥
     * @param base64Encode Base64编码后的密文
     * @return 加密前的字符串
     */
    @SneakyThrows
    public static String privateKeyDecrypt(RSAKeySizeEnum keySizeEnum, PrivateKey privateKey, String base64Encode) {
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // 分段解密
        return sectionDecrypt(base64Encode, cipher, keySizeEnum.getMaxDecryptBlock());
    }

    /**
     * 生成签名
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 验证数据签名
     * @param srcData
     * @param publicKey
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * 分段加密
     * @param encryptStr 需要加密的字符串
     * @param cipher 密码器
     * @return 加密后的数据
     */
    @SneakyThrows
    public static String sectionEncrypt(String encryptStr, Cipher cipher, int maxEncryptBlock) {
        // 直接加密base64的字符串
        byte[] data = encryptStr.getBytes();
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxEncryptBlock) {
                cache = cipher.doFinal(data, offSet, maxEncryptBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxEncryptBlock;
        }
        // 加密数据, 返回加密后的密文
        byte[] encryptedData = out.toByteArray();
        out.close();

        // 加密后将其Base64编码，防止中文乱码
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * 分段解密
     * @param base64Encode Base64编码后的密文
     * @param cipher 密码器
     * @return 返回解密后的数据
     */
    @SneakyThrows
    public static String sectionDecrypt(String base64Encode, Cipher cipher, int maxDecryptBlock) {
        byte[] data = Base64.getDecoder().decode(base64Encode);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxDecryptBlock) {
                cache = cipher.doFinal(data, offSet, maxDecryptBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxDecryptBlock;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();

        // 加密的字符串是使用Base64编码,所以这里解密后还需要解码才是真实数据
        return new String(decryptedData);
    }


    /**
     * Key长度枚举
     */
    @Getter
    static enum KeySizeEnum{

    }
}