package com.goudong.message.util;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.models.*;
import com.goudong.commons.utils.AssertUtil;

public class SendSms {

    /**
     * AK
     */
    private static final String ACCESS_KEY_ID = "LTAI5tJj15oz8yKAsYybgyT3";
    /**
     * SK
     */
    private static final String ACCESS_KEY_SECRET = "9wxNyPg3p9sb5hYFCx1h7MY6PUWWC2";

    /**
     * 签名名称
     */
    public static final String SIGN_NAME = "陈飞龙的网上学习";

    /**
     * 模版CODE
     */
    public static final String TEMPLATE_CODE = "SMS_217235527";

    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    public static Client createClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(ACCESS_KEY_ID)
                // 您的AccessKey Secret
                .setAccessKeySecret(ACCESS_KEY_SECRET);
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
    public static void  sendCode(String phone, String code) throws Exception {
        AssertUtil.isPhone(phone, "手机号格式错误");
        AssertUtil.hasLength(code, "验证码错误");
        Client client = SendSms.createClient();
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                // 签名名称
                .setSignName(SIGN_NAME)
                // 变量替换
                .setTemplateParam("{code:'"+ code +"'}")
                // 模版CODE
                .setTemplateCode(TEMPLATE_CODE);
        // 复制代码运行请自行打印 API 的返回值
        client.sendSms(sendSmsRequest);
    }

}
