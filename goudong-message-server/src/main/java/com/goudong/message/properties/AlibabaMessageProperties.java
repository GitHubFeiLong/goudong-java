package com.goudong.message.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 类描述：
 * 阿里巴巴的短信配置
 * @Author e-Feilong.Chen
 * @Date 2021/12/8 12:59
 */
@Data
@ConfigurationProperties("message.alibaba.message")
public class AlibabaMessageProperties {
    /**
     * 访问密钥Id
     */
    private String accessKeyId;
    /**
     * 访问密钥
     */
    private String accessKeySecret;

    /**
     * 签名名称
     */
    private String signName;

    /**
     * 模版CODE
     */
    private String templateCode;
}
