package com.goudong.maven.plugin.rsa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 类描述：
 * RSA 工具类（生成/保存密钥对、加密、解密）
 * @author msi
 * @date 2022/2/4 18:07
 * @version 1.0
 */
public class RSAUtil {

    private static final Logger log = LoggerFactory.getLogger(RSAUtil.class);
    /**
     * 算法名称
     */
    private static final String ALGORITHM = "RSA";

    /**
     * 密钥长度
     */
    private static final int KEY_SIZE = 1024;

    /**
     * 私钥文件保存文件位置
     */
    public static final String PRIVATE_KEY_PATH = new StringBuilder()
            .append(System.getProperty("user.dir"))
            .append(File.separator)
            .append("src")
            .append(File.separator)
            .append("main")
            .append(File.separator)
            .append("resources")
            .append(File.separator)
            .append(".ssh")
            .append(File.separator)
            .append("server")
            .append(File.separator)
            .append("rsa")
            .toString();
    /**
     * 公钥文件保存文件位置
     */
    public static final String PUBLIC_KEY_PATH = new StringBuilder()
            .append(System.getProperty("user.dir"))
            .append(File.separator)
            .append("src")
            .append(File.separator)
            .append("main")
            .append(File.separator)
            .append("resources")
            .append(File.separator)
            .append(".ssh")
            .append(File.separator)
            .append("server")
            .append(File.separator)
            .append("rsa.pub")
            .toString();

    /**
     * 判断默认目录下是否存在公钥和私钥文件，当不存在时创建随机的密钥文件
     * @throws Exception
     */
    public static void generateKeyPair2File() throws Exception {
        log.info("判断保存私钥和公钥的文件是否存在...");
        // 1. 检查文件是否存在
        File publicKeyFile = new File(PUBLIC_KEY_PATH);
        File privateKeyFile = new File(PRIVATE_KEY_PATH);
        // 当公钥或私钥不存在时，创建新的密钥
        if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
            log.info("密钥文件不存在!即将新生成密钥信息保存到文件中:\n\t公钥:{}\n\t私钥:{}", PUBLIC_KEY_PATH, PRIVATE_KEY_PATH);
            KeyPair keyPair = generateKeyPair();
            saveKeyForEncodedBase64(keyPair);
            log.info("密钥信息存储成功");
            return;
        }
        log.info("密钥存储的文件已存在。\n你也可以将密钥文件删除后，再执行。\n密钥文件位置：\n\t公钥:{}\n\t私钥:{}", PUBLIC_KEY_PATH, PRIVATE_KEY_PATH);
    }

    /**
     * 随机生成密钥对（包含公钥和私钥）
     */
    public static KeyPair generateKeyPair() throws Exception {
        log.debug("开始使用{}生成密钥", ALGORITHM);
        // 获取指定算法的密钥对生成器
        KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM);

        // 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
        gen.initialize(KEY_SIZE, new SecureRandom());
        // 随机生成一对密钥（包含公钥和私钥）
        KeyPair keyPair = gen.generateKeyPair();
        log.debug("生成密钥成功");
        return keyPair;
    }

    /**
     * 保存密钥到项目根目录
     * @param keyPair
     * @throws IOException
     */
    private static void saveKeyForEncodedBase64(KeyPair keyPair) throws IOException {
        log.debug("开始保存公钥到文件...");
        saveKeyForEncodedBase64(keyPair.getPublic(), new File(PUBLIC_KEY_PATH));
        log.debug("保存公钥到文件成功...");
        log.debug("开始保存私钥到文件...");
        saveKeyForEncodedBase64(keyPair.getPrivate(), new File(PRIVATE_KEY_PATH));
        log.debug("保存私钥到文件成功...");
    }

    /**
     * 将 公钥/私钥 编码后以 Base64 的格式保存到指定文件
     * @param key 公钥/私钥
     * @param keyFile 保存公钥的文件
     * @throws IOException
     */
    public static void saveKeyForEncodedBase64(Key key, File keyFile) throws IOException {
        // 获取密钥编码后的格式
        byte[] encBytes = key.getEncoded();
        // 转换为 Base64 文本
        String encBase64 = Base64.getEncoder().encodeToString(encBytes);
        log.debug("开始写入文件");
        // 保存到文件
        writeFile(encBase64, keyFile);
        log.debug("结束写入文件");
    }

    /**
     * 写文件
     * @param data
     * @param file
     * @throws IOException
     */
    public static void writeFile(String data, File file) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(data.getBytes());
            out.flush();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("执行写文件时，关闭文件输出流失败：{}", e.getMessage());
                }
            }
        }
    }

}
