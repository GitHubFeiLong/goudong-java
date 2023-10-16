package com.goudong.core.security.cer;

import sun.security.x509.*;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class CertificateUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 创建证书
     * @param issuer 颁发者
     * @param subject 使用者
     * @param validTime 有效截止时间
     * @return cert
     */
    public static Cer create(String issuer, String subject, Date validTime) {
        try {
            // 生成密钥对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 创建证书信息
            X509CertInfo certInfo = new X509CertInfo();
            Date startDate = new Date();

            // 设置证书版本
            CertificateVersion certVersion = new CertificateVersion(2);
            certInfo.set(X509CertInfo.VERSION, certVersion);

            // 设置证书序列号
            BigInteger serialNumber = new BigInteger(64, new SecureRandom());
            CertificateSerialNumber certSerialNumber = new CertificateSerialNumber(serialNumber);
            certInfo.set(X509CertInfo.SERIAL_NUMBER, certSerialNumber);

            // 设置证书颁发者
            certInfo.set(X509CertInfo.ISSUER, new X500Name("CN=" + Optional.ofNullable(issuer).orElseGet(() -> "Issuer")));

            // 设置证书主题
            certInfo.set(X509CertInfo.SUBJECT, new X500Name("CN="  + Optional.ofNullable(subject).orElseGet(() -> "Subject")));

            // 设置证书有效期
            CertificateValidity certValidity = new CertificateValidity(startDate, validTime);
            certInfo.set(X509CertInfo.VALIDITY, certValidity);

            // 设置证书公钥
            CertificateX509Key certPublicKey = new CertificateX509Key(keyPair.getPublic());
            certInfo.set(X509CertInfo.KEY, certPublicKey);

            // 设置证书算法
            AlgorithmId algorithmId = AlgorithmId.get("SHA256WithRSA");
            CertificateAlgorithmId certAlgorithmId = new CertificateAlgorithmId(algorithmId);
            certInfo.set(X509CertInfo.ALGORITHM_ID, certAlgorithmId);

            // 创建证书对象
            X509CertImpl cert = new X509CertImpl(certInfo);
            cert.sign(keyPair.getPrivate(), "SHA256withRSA");
           String encode = Base64.getEncoder().encodeToString(cert.getEncoded());
           System.out.println(encode);

            // 将证书保存到文件
           // OutputStream outputStream = new FileOutputStream("certificate.cer");
           // outputStream.write(Base64.getDecoder().decode(encode));
           // outputStream.close();

            System.out.println("证书生成成功！");
            return new Cer(cert, keyPair);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载证书
     * @param inputStream 证书的输入流
     * @return 证书
     */
    public static X509Certificate loadCertificate(InputStream inputStream) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
            // 检查证书是否有效期内
            cert.checkValidity();
            return cert;
        } catch (CertificateExpiredException e) {
            throw new RuntimeException("证书已过期", e);
        } catch (CertificateNotYetValidException e) {
            throw new RuntimeException("证书尚未生效", e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载证书
     * @param encode Base64编码后的证书
     * @return 证书
     */
    public static X509Certificate loadCertificate(String encode) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X509");

            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(encode)));
            // 检查证书是否有效期内
            cert.checkValidity();
            return cert;
        } catch (CertificateExpiredException e) {
            throw new RuntimeException("证书已过期", e);
        } catch (CertificateNotYetValidException e) {
            throw new RuntimeException("证书尚未生效", e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 类描述：
     * 生成证书相关信息
     * @author chenf
     * @version 1.0
     */
    public static class Cer {
        /**
         * 证书
         */
        private X509Certificate cer;

        /**
         * 密钥
         */
        private KeyPair keyPair;

        public Cer(X509Certificate cer, KeyPair keyPair) {
            this.cer = cer;
            this.keyPair = keyPair;
        }

        public X509Certificate getCer() {
            return cer;
        }

        public void setCer(X509Certificate cer) {
            this.cer = cer;
        }

        public KeyPair getKeyPair() {
            return keyPair;
        }

        public void setKeyPair(KeyPair keyPair) {
            this.keyPair = keyPair;
        }
    }
}
