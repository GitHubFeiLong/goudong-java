package com.goudong.authentication.server.service.cer;

import com.goudong.core.security.cer.CertificateUtil;
import sun.security.x509.*;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
public class GenerateCerCertificateExample {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, CertificateException, SignatureException, InvalidKeyException, NoSuchProviderException {
        CertificateUtil.create("goudong", "应用三", 365);
    }
}
