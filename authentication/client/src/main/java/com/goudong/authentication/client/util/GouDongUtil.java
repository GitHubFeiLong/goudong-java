package com.goudong.authentication.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 */
public class GouDongUtil {
    //~fields
    //==================================================================================================================
    private static final Logger log = LoggerFactory.getLogger(GouDongUtil.class);

    //~methods
    //==================================================================================================================
    /**
     * 随机字符
     * @param length
     * @return
     */
    public static String randomStr(int length) {
        if (length < 1) {
            throw new RuntimeException("参数length需要大于0");
        }
        /*
            用于随机选的字符
         */
        final String baseNumberChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(baseNumberChar.charAt(random.nextInt(baseNumberChar.length())));
        }
        return sb.toString();
    }

    /**
     * 检查是否能访问
     * @param uri 资源uri
     * @param method 请求方式
     * @param userRoles 用户角色
     * @return true有权限；false没权限
     */
    private boolean check(String uri, String method, List<String> userRoles, List<PermissionDTO> permissionDTOList) {
        // 查询应用菜单权限
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<String> permissionRoles = permissionDTOList.parallelStream().filter(f -> {
                    if (f.getType() == 2 || f.getType() == 3) { // 按钮和接口
                        return antPathMatcher.match(f.getPath(), uri) && f.getMethod().toUpperCase().contains(method.toUpperCase());
                    }
                    return false;
                })
                // 扁平化角色
                .flatMap(m -> m.getRoles().stream())
                .collect(Collectors.toList());

        log.info("url={}，method={}，用户权限={}，需要权限={}", uri, method, userRoles, permissionRoles);
        if (CollectionUtil.isEmpty(permissionRoles)) {
            return true;
        }

        return permissionRoles.stream().anyMatch(userRoles::contains);
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
            String publicKeyBase64 = Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded());
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
     * 加载私钥
     * @param inputStream 私钥的输入流
     * @return 私钥
     */
    public static PrivateKey loadPrivateKey(InputStream inputStream) {
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                array.write(buffer, 0, length);
            }

            String privateKey = array.toString("utf-8").replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        } catch (IOException e) {
            throw new RuntimeException("无效的密钥");
        }
    }

    /**
     * 加载公钥
     * @param inputStream 公钥的输入流
     * @return 公钥
     */
    public static PublicKey loadPublicKey(InputStream inputStream) {
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                array.write(buffer, 0, length);
            }

            String publicKey = array.toString("utf-8").replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "").replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        } catch (IOException e) {
            throw new RuntimeException("无效的密钥");
        }
    }

    /**
     * 使用客户端生成的私钥生成签名
     * @param body 请求体
     * @param privateKey 私钥
     * @return
     */
    public static String sign(String body, PrivateKey privateKey) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(body.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持SHA256withRSA", e);
        } catch (SignatureException e) {
            throw new RuntimeException("签名计算失败", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("无效的私钥", e);
        }
    }

    /**
     * 使用服务端颁发的证书校验server端的签名
     * @param body server端响应的数据
     * @param authorization server端响应头{@code Authorization}的值
     * @param certificateInputStream server端颁发的证书输入流
     * @return
     */
    public boolean verify(String body, String authorization, InputStream certificateInputStream) {
        log.debug("使用证书校验签名开始");
        log.info("请求头 Authorization：{}", authorization);
        // 使用正则表达式，提取关键信息
        Pattern pattern = Pattern.compile("GOUDONG-SHA256withRSA timestamp=\"(\\d+)\",nonce_str=\"(.*)\",signature=\"(.*)\"");
        Matcher matcher = pattern.matcher(authorization);
        // matcher.matches() 方法必须调用，不然group(i)会报错
        if (!matcher.matches()) {
            new RuntimeException("请求头Authorization的格式错误");
        }
        // 从authorization获取关键信息
        String timestamp = matcher.group(1);
        String nonce = matcher.group(2);
        String signature = matcher.group(3); // 拉卡拉返回的签名

        log.debug("timestamp:{},nonce_str:{},signature:{}", timestamp, nonce, signature);

        String preSignData = timestamp+"\n"+nonce+"\n"+body+"\n"; // 验签前的字符串
        byte[] message = preSignData.getBytes(StandardCharsets.UTF_8); // 转换byte[]

        // 加载证书
        X509Certificate certificate = loadCertificate(certificateInputStream);
        try {

            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(certificate); // 证书验签
            sign.update(message);
            byte[] signatureB = Base64.getDecoder().decode(signature); // 拉卡拉返回的签名转byte[]
            boolean verify = sign.verify(signatureB); // 验签,验拉卡拉返回的签名

            log.debug("验证签名结束，验证结果：{}", verify);
            return verify;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前java环境不支持SHA256withRSA", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("无效的证书", e);
        } catch (SignatureException e) {
            throw new RuntimeException("签名验证过程发生了错误", e);
        }
    }
}
