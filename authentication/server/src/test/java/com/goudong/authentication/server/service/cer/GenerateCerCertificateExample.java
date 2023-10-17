package com.goudong.authentication.server.service.cer;

import cn.hutool.core.util.RandomUtil;
import com.goudong.core.security.cer.CertificateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import sun.security.x509.*;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
@ExtendWith({})
public class GenerateCerCertificateExample {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, CertificateException, SignatureException, InvalidKeyException, NoSuchProviderException {
        CertificateUtil.create("goudong", "应用三", new Date());
    }

    @Test
    public void test1() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDJOvNB6fFRGIYDK2INZ6QnXkGhkGmBaxEG6wizQ6Kbkxg13/UIhrWxAwMCX7RYHCYbPqP0NIQIB7bApGbsKTXUK1DG+abAV3okrkgmgWkavPME4Gpth9khZKg4eQUiavk1Xjj+HFwQzhWKDRYApWnkM8ccFWCHr2qYV2cDHq+LouSBlan8tAjWVDr/J8tFjGI7+5yO2ON5XMraoqvhTBcyrW5eSOj208QAWc2YD2hUTuPn4IA66ZLcaTh4AddQpmD1IJTHrqhyNcLEFjTRu4/FGjPxWAg8rdKQYZS0XJiFZaG+/qaijleaL6URjbHlxc28I43a0ODM3ShCLoVcJYhBAgMBAAECggEBAKCtq5OUI5ZniIp7V53whcjRoIJLd5NZVI07mwTZGrwy5gAOxe968RVYnEyG8s/wGhVjHTNfw5VN44VLQMuN1GeH9sYKjjXDI8qqRv5IEw7AlSPttc2jzaK2ZwRAzfK/jCIrDEMLjg/1HonPDSSS8EzBc1lfUGbHkMv37k81b2KE0DMiPSXHX85jF6oHn1syD7JvMFFgtecp9YYmZQPxJTJ1He9RtTdUqLnHd4jhMckXyMw8Bz4OqKtH5Z8WXYXY6AORgIzbTA3SLD7VhEEYRdtfJwrvYpzD6OVG7DtbApSCapvq2jrUS8nW90DYgyN1wyme0LXraU693TGIcZHw9YECgYEA/loNcGcX/pcmFg/2mw2oTuS/43hCYLBECWCC112Yxu6Ki1gZGXF9ufDASykRZgiOrNLN9TZDmvn+FFSTA/KZHl4YhBXx37w4dh7gU/DkyDXoOzlZaKyAxpqISVvNtjnQaMOOi+eodspAFaCLVyq9HxkycpYsjYKvntpkqDB0VskCgYEAyojGFpOy91NZwGhLAHBRhoaugYWSxGDlqPep/rYi8bICf+jTl90wE60wy0Yy6v0l1T3ZLn/ojhl1/L4ygQENW8+a9mmcLdfhXZYf5eoBCsIOvzUj9iRBVKtxIDTmIPbqocmUubZ+GHkePBeI+7K5C/C5qJroSgsGmzShIyT3ybkCgYAMDPYwCiJf5Idgajcz12zO+BWDUHJMpG1EyHbKf1u6qRiVS4I861yaj6Z8qSBy5hrkY0RwxrZjRE7eZbdla3wHrUuaDnQcNo2yQvo73+Dm/27WR3xN8kZTooSsfRX8+TY/kGxTgpE/t4tbuvR/F75IF+iE90p7FeL+MrisI0ePyQKBgQC2QumIbbhTTEXrma9NIMas+kzWMD9IzC5ApUfs+TlvKhOHyh2If7uBKY1MkMrHn7JJORRV/TNplU4ReZP3ddvwsF4yvA2ZT7n2v1/LRo9TgBlHRuoq2quUJ4hxREmIhsvVdpOJoMnO+kgUKdXTNB3EughJy8WYxF1YlL8h4mW+uQKBgQC9HSfFAxqvgI7G9YQ+F/l7XbRNn4KEZNMAiCCcL4Yr4uW00HHJOp2T0M3xRzIJ9k2ke2o/sgzblboT1GfMddoGCPBYR+4mCfCH7vqOW0K7HVF30zMvQrx9IVr/JSZJ5jnlwTYWzMXFYwalHDpRf7EacFbdXZlL22tsHnzwnrj4rw==";
        Signature signature = Signature.getInstance("SHA256WithRSA");
        KeyFactory kf = KeyFactory.getInstance("RSA");
        signature.initSign(kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))));
        Long appid = 1L;
        String serial_number = "3143715379b2a87b";
        String timestamp = new Date().getTime() + "";
        String nonce_str = RandomUtil.randomString(12);
        String body = "{}";
        String message = appid + "\n" + serial_number + "\n" + timestamp + "\n" + nonce_str + "\n" + body + "\n";
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        String sign = Base64.getEncoder().encodeToString(signature.sign());
        String authenization = String.format("GOUDONG-SHA256withRSA appid=\"%s\",serial_number=\"%s\",timestamp=\"%s\",nonce_str=\"%s\",signature=\"%s\"",
                appid,
                serial_number,
                timestamp,
                nonce_str,
                sign
                );

        System.out.println("sign = " + sign);
        System.out.println("authenization = " + authenization);
    }
}
