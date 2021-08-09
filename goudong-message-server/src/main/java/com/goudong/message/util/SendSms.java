package com.goudong.message.util;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import com.goudong.commons.utils.AssertUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alibaba.message")
public class SendSms {

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

    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    public  Client createClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(this.accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(this.accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    /**
     * 发送注册验证码
     * @param phone 手机号
     * @param code 验证码
     * @throws Exception
     */
    public void sendCode(String phone, String code) throws Exception {
        AssertUtil.isPhone(phone, "手机号格式错误");
        AssertUtil.hasLength(code, "验证码错误");
        Client client = createClient();
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                // 签名名称
                .setSignName(this.signName)
                // 变量替换
                .setTemplateParam("{code:'"+ code +"'}")
                // 模版CODE
                .setTemplateCode(this.templateCode);
        // 复制代码运行请自行打印 API 的返回值
        client.sendSms(sendSmsRequest);
    }

}
