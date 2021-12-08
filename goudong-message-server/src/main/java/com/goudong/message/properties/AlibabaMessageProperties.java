package com.goudong.message.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2021/12/8 12:59
 */
@Data
@ConfigurationProperties("message.alibaba.message")
public class AlibabaMessageProperties {
    /**
     * AK
     */
    private String accessKeyId;
    /**
     * SK
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
