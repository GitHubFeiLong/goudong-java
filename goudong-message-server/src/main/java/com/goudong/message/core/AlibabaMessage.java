package com.goudong.message.core;

import lombok.Data;

/**
 * 类描述：
 * 阿里巴巴的短信配置信息
 * @author msi
 * @version 1.0
 * @date 2021/12/8 19:58
 */
@Data
public class AlibabaMessage {
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
