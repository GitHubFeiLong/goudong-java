package com.goudong.message.properties;

import com.goudong.commons.exception.core.ApplicationBootFailedException;
import com.goudong.commons.utils.core.LogUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 类描述：
 * 阿里巴巴的短信配置
 * @Author e-Feilong.Chen
 * @Date 2021/12/8 12:59
 */
@Slf4j
@Data
@Component
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

    /**
     * 参数检查，四个值都不能为空
     */
    @PostConstruct
    public void init() {
        boolean boo = StringUtils.isBlank(accessKeyId)
                && StringUtils.isBlank(accessKeySecret)
                && StringUtils.isBlank(signName)
                && StringUtils.isBlank(templateCode);
        if (boo) {
            LogUtil.error(log, "");
            throw new ApplicationBootFailedException("goudong-message-server启动失败",
                    "message.alibaba.message的4个属性都需要配置正确格式",
                    "请配置正确的阿里云短信");
        }
    }

}
