package com.goudong.core.security.rsa;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

/**
 * 类描述：
 * 后端服务的RSA
 * @author msi
 * @version 1.0
 * @date 2022/2/9 20:42
 */
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
    private static final String PUBLIC_KEY_PATH = new StringBuilder().append(".ssh").append(File.separator).append("server").append(File.separator).append("rsa.pub").toString();

    /**
     * 私钥文件保存文件位置
     */
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
    public static ServerRSA getInstance(){
        try {
            if (serverRSAKey == null) {
                synchronized (RSAKey.class) {
                    if (serverRSAKey == null) {
                        PublicKey publicKey = getPublicKeyByFile();
                        PrivateKey privateKey = getPrivateKeyByFile();
                        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

                        // 通过反射获取生成RSA的key长度(这是过期的)
                        // Field n = RSAPublicKeyImpl.class.getDeclaredField("n");
                        // n.setAccessible(true);
                        // BigInteger bigInteger = (BigInteger)n.get(publicKey);
                        // int keySize = bigInteger.bitLength();

                        int keySize = KeyFactory.getInstance(RSAUtil.ALGORITHM)
                                .getKeySpec(publicKey, RSAPublicKeySpec.class)
                                .getModulus()
                                .toString(2)
                                .length();

                        serverRSAKey = new ServerRSA(publicKey, privateKey, publicKeyBase64, privateKeyBase64, keySize);
                    }
                }
            }
            return serverRSAKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取文件内容，并转换成公钥对象
     * @return 公钥对象
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getPublicKeyByFile() {

        final String publicKeyBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgMMsJimfsZqN//iyRVfC/v2Yec7mf5lUeuFIsrquofl5VFwoUx13UQvV8WI4alHNCXKsnvNuIqQ7ESOznQ3aS4ZEvhpwnjMxDDvjZ9xCXRVU06e7ppI8I8neT2PtSh7IvV3gSRauLxMRkNw5UIhiPQ4hps0kLzfZwsRm17/FE/gBbN1MvNY77JneRgwsbuMIUaxn22Aqq2uZgTOOlLsHaGnthDBXb7QPlngxe/wAQF5cckJg4qzQ2AUtBV9PXrnK07cBT+cUWTYhKrvK2VWDXEgmv8b62WacpPVLuCJE9JWmuVqTgYhzTGfXGBr8l8hjCO8FOBzGPmgZTee3HcAcDQIDAQAB";
        return (PublicKey) RSAUtil.base642Key(publicKeyBase64, true);

        //ClassPathResource publicClassPathResource = new ClassPathResource(PUBLIC_KEY_PATH);
        //// 公钥文件是否存在
        //if (publicClassPathResource.exists()) {
        //    File publicKeyFile = publicClassPathResource.getFile();
        //    // 公钥文件存储时使用Base64编码后保存d的
        //    String publicKeyBase64 = IOUtil.readFile(publicKeyFile);
        //
        //    return (PublicKey) RSAUtil.base642Key(publicKeyBase64, true);
        //}
        //
        //String errorMessage = StrUtil.format("保存公钥的资源文件不存在:{}", PUBLIC_KEY_PATH);
        //throw new FileNotFoundException(errorMessage);
    }

    /**
     * 获取文件内容，并转换成私钥对象
     * @return 私钥对象
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PrivateKey getPrivateKeyByFile()  {
        // 公钥文件存储时使用Base64编码后保存d的
        String privateKeyBase64 = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCAwywmKZ+xmo3/+LJFV8L+/Zh5zuZ/mVR64Uiyuq6h+XlUXChTHXdRC9XxYjhqUc0Jcqye824ipDsRI7OdDdpLhkS+GnCeMzEMO+Nn3EJdFVTTp7umkjwjyd5PY+1KHsi9XeBJFq4vExGQ3DlQiGI9DiGmzSQvN9nCxGbXv8UT+AFs3Uy81jvsmd5GDCxu4whRrGfbYCqra5mBM46Uuwdoae2EMFdvtA+WeDF7/ABAXlxyQmDirNDYBS0FX09eucrTtwFP5xRZNiEqu8rZVYNcSCa/xvrZZpyk9Uu4IkT0laa5WpOBiHNMZ9cYGvyXyGMI7wU4HMY+aBlN57cdwBwNAgMBAAECggEAedItjt2KjMmg1zA/2YMypXTgMT4irU4vsyI5WX9tgSk6NSoLrLcQD8mW3A0FOvGxfuLTln7REE045PpWEJzujs21c/Yn3kSoft6aQ8ULtG1eF/F1hB6Ob32pqorsEVgWq7KJZBzlJvxvfhIc16hw6TrZc6paNaItkCuo8S4qEr0M1FbfzfWNpa1BcahqizpAyELoXCBHTaspVPYmCbzMLLaq+9gT1oitbuSb09JKvlkH3CxvWNGb7XX010VpTq4uFX/TqcCReF7LVAr411STRMf3L0NcacXRTVy29tViajj8VaOqKjWi3u9/3Z4NFeDX6G3u1cnwSnVIkwYEYI8RAQKBgQDjAn4SaAhuWw9jjUbksjfLfrIYxgfX1YXSjjpUuqndqa9JjbVvGdFogAaDv1+TUUJogA5g9K5hGpF/oFldoQvs2O6vaicAxkfgJvjUmYZCE3EgRI97FhYe4mGchhCZLcBLXKZ2raaNFN1WKI5WS40TrAAnxeK9+CPfDJzUozeInQKBgQCRNLt2waYrISkTwTw0URYjujTaLV8t/7hU8fOl5NK6bWFxoYwI9V6EC2MVqg5NhQzmCRNE00j7fo4CZICADQUHzA2x5JwcH/fd8hS5bmjEsHVnqL0CGFuX23TulQ8SrwVr8uyxq5HpkTEHrXK+KY8AQCf+YN280MWYE2tjiIHuMQKBgDzLOxYU1EUxj8J6YET40WZm7K0jw4/yt972hfqQ7tLVEYNiNvek82bH+nan9qGOPnmb6b3faR/KLLMAL06axYXNZsaFaCKV90O5TCQrZUAm0sHwenhdJtloiPmREbrj/L5x9oaL5LGdp9TeEZhcrVBaXMRKA3oajQi76PgtKb8lAoGAMxBPSJELJamwr2DKEj/dwEywX3WLPjrqkPzRSSqyLJtgpDxCabgYN+llO+4kv4AlrBPO8eo3hTHZMOA1DFMHzNkmi8OwXnejjCqgvSEluN2xO/XsGfuE4l9rvKcwuMpR5sd0E3sZggDsNB379wHYZycoqV1ZPRhSFIvnvQX5Y1ECgYAlFwc1twrEMJJaqtagk7s7rdNFTbGd9GHen8Mj1bA+zO8hHVVvE2o9xZsTqm0k/yCPvatjjuk1BNLJconQgNxwnK1q07H7rYMSgeyifjCLHoMF8YzA4XIGDM+EFxhlBdA/+M/B6JffFBrx5EjHrCvQHW1COjsLUdBeQHWqxEBk9Q==";

        return (PrivateKey) RSAUtil.base642Key(privateKeyBase64, false);

        //ClassPathResource privateClassPathResource = new ClassPathResource(PRIVATE_KEY_PATH);
        //// 私钥文件是否存在
        //if (privateClassPathResource.exists()) {
        //    File privateKeyFile = privateClassPathResource.getFile();
        //    // 公钥文件存储时使用Base64编码后保存d的
        //    String privateKeyBase64 = IOUtil.readFile(privateKeyFile);
        //
        //    return (PrivateKey) RSAUtil.base642Key(privateKeyBase64, false);
        //}
        //
        //String errorMessage = StrUtil.format("保存私钥的资源文件不存在:{}", PRIVATE_KEY_PATH);
        //throw new FileNotFoundException(errorMessage);
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

    public static ServerRSA getServerRSAKey() {
        return serverRSAKey;
    }

    public static void setServerRSAKey(ServerRSA serverRSAKey) {
        ServerRSA.serverRSAKey = serverRSAKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKeyBase64() {
        return publicKeyBase64;
    }

    public void setPublicKeyBase64(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }

    public String getPrivateKeyBase64() {
        return privateKeyBase64;
    }

    public void setPrivateKeyBase64(String privateKeyBase64) {
        this.privateKeyBase64 = privateKeyBase64;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }
}
