package com.goudong.message.autoconfigure;

import com.goudong.message.core.AlibabaMessage;
import com.goudong.message.properties.AlibabaMessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * 啊里巴巴短信自动配置
 * @author msi
 * @version 1.0
 * @date 2021/12/8 19:52
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AlibabaMessageProperties.class)
public class AlibabaMessageAutoConfiguration {

    private final AlibabaMessageProperties alibabaMessageProperties;

    public AlibabaMessageAutoConfiguration(AlibabaMessageProperties alibabaMessageProperties) {
        this.alibabaMessageProperties = alibabaMessageProperties;
    }

    @Bean
    public AlibabaMessage alibabaMessage() {
        AlibabaMessage alibabaMessage = new AlibabaMessage();
        alibabaMessage.setAccessKeyId(alibabaMessageProperties.getAccessKeyId());
        alibabaMessage.setAccessKeySecret(alibabaMessageProperties.getAccessKeySecret());
        alibabaMessage.setSignName(alibabaMessageProperties.getSignName());
        alibabaMessage.setTemplateCode(alibabaMessageProperties.getTemplateCode());

        return alibabaMessage;
    }

}
