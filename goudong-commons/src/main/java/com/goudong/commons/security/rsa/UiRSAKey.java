// package com.goudong.commons.security.rsa;
//
// import cn.hutool.core.util.StrUtil;
// import com.goudong.commons.exception.security.rsa.RSAException;
// import com.goudong.commons.utils.core.IOUtil;
// import lombok.SneakyThrows;
// import org.springframework.core.io.ClassPathResource;
// import sun.security.rsa.RSAPublicKeyImpl;
//
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.lang.reflect.Field;
// import java.math.BigInteger;
// import java.security.KeyFactory;
// import java.security.NoSuchAlgorithmException;
// import java.security.PublicKey;
// import java.security.spec.InvalidKeySpecException;
// import java.security.spec.X509EncodedKeySpec;
// import java.util.Base64;
//
// /**
//  * 类描述：
//  * 前端服务的RSA
//  * @author msi
//  * @version 1.0
//  * @date 2022/2/9 20:42
//  */
// public class UiRSAKey implements RSA {
//
//     //~fields
//     //==================================================================================================================
//     /**
//      * 单例
//      */
//     private static UiRSAKey uiRSAKey;
//
//     /**
//      * 公钥文件保存文件位置
//      */
//     private static final String PUBLIC_KEY_PATH = new StringBuilder().append(".ssh").append(File.separator).append("ui").append(File.separator).append("rsa.pub").toString();
//
//     /**
//      * 公钥
//      */
//     private PublicKey publicKey;
//
//     /**
//      * 公钥Base64字符串
//      */
//     private String publicKeyBase64;
//
//     /**
//      * 生成密钥的key长度(1024 or 2048)
//      */
//     private int keySize;
//
//     //~methods
//     //==================================================================================================================
//     private UiRSAKey(PublicKey publicKey, String publicKeyBase64, int keySize) {
//         this.publicKey = publicKey;
//         this.publicKeyBase64 = publicKeyBase64;
//         this.keySize = keySize;
//     }
//
//     /**
//      * 使用加锁双检单例-懒汉式，获取RSAKey对象
//      * @return
//      * @throws IOException
//      * @throws NoSuchAlgorithmException
//      * @throws InvalidKeySpecException
//      */
//     @SneakyThrows
//     public static UiRSAKey getInstance() {
//         if (uiRSAKey == null) {
//             synchronized (UiRSAKey.class) {
//                 if (uiRSAKey == null) {
//                     PublicKey publicKey = getPublicKeyByFile();
//                     String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//
//                     // 通过反射获取生成RSA的key长度
//                     Field n = RSAPublicKeyImpl.class.getDeclaredField("n");
//                     n.setAccessible(true);
//                     BigInteger bigInteger = (BigInteger)n.get(publicKey);
//
//                     uiRSAKey = new UiRSAKey(publicKey, publicKeyBase64, bigInteger.bitLength());
//                 }
//             }
//         }
//         return uiRSAKey;
//     }
//
//     /**
//      * 获取文件内容，并转换成公钥对象
//      * @return 公钥对象
//      * @throws IOException
//      * @throws NoSuchAlgorithmException
//      * @throws InvalidKeySpecException
//      */
//     private static PublicKey getPublicKeyByFile() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//         ClassPathResource publicClassPathResource = new ClassPathResource(PUBLIC_KEY_PATH);
//         // 公钥文件是否存在
//         if (publicClassPathResource.exists()) {
//             File publicKeyFile = publicClassPathResource.getFile();
//             // 公钥文件存储时使用Base64编码后保存d的
//             String publicKeyBase64 = IOUtil.readFile(publicKeyFile);
//
//             // 把 公钥的Base64文本进行解码
//             byte[] encPubKey = Base64.getDecoder().decode(publicKeyBase64);
//
//             // 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
//             return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encPubKey));
//         }
//
//         String errorMessage = StrUtil.format("保存公钥的资源文件不存在:{}", PUBLIC_KEY_PATH);
//         throw new FileNotFoundException(errorMessage);
//     }
//
//
//     /**
//      * 公钥加密
//      *
//      * @param data      加密的字节数组
//      * @return 加密后Base64编码后的字符串
//      */
//     @Override
//     public String publicKeyEncrypt(byte[] data) {
//         RSA.KeySizeEnum byKeySize = RSA.KeySizeEnum.getByKeySize(keySize);
//         return RSAUtil.publicKeyEncrypt(byKeySize, publicKey, data);
//     }
//
//     /**
//      * 私钥解密
//      *
//      * @param base64Encode 已经加密好的Base64字符串
//      * @return 解码后的字符数组
//      */
//     @Override
//     public byte[] privateKeyDecrypt(String base64Encode) {
//         throw new RSAException("未配置ui的私钥，使用私钥进行解码错误");
//     }
// }