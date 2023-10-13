package com.goudong.authentication.server.service.rsa;

import com.goudong.core.security.rsa.RSAKeySizeEnum;
import com.goudong.core.security.rsa.RSAUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.security.KeyPair;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
@ExtendWith({})
public class Demo {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    void testRSA() {
        KeyPair keyPair = RSAUtil.generateKeypair(RSAKeySizeEnum.RSA2048);

        System.out.println("keyPair = " + keyPair);
    }
}
