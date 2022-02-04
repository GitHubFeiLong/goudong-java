package com.goudong.oauth2.config;

import com.goudong.commons.utils.core.RSAUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.security.KeyPair;

/**
 * 类描述：
 * RSA启动时RSA算法
 * @author msi
 * @version 1.0
 * @date 2022/2/4 18:45
 */
@Component
public class RSAApplicationRunner implements ApplicationRunner {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Override
    public void run(ApplicationArguments args) throws Exception {
        KeyPair keyPair = RSAUtil.generateKeyPair();
        RSAUtil.saveKeyForEncodedBase64(keyPair);
    }
}