package com.goudong.message.util;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.models.*;

public class SendSms {

    /**
     * AK
     */
    private static final String ACCESS_KEY_ID = "LTAI5tQPPaJJKAWUJ454Y8QX";
    /**
     * SK
     */
    private static final String ACCESS_KEY_SECRET = "OIoXAh9GdJLh9yHJimxXf5GYB3LSYf";

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

    public static void main(String[] args_) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(args_);
        Client client = SendSms.createClient();
        AddSmsSignRequest addSmsSignRequest = new AddSmsSignRequest()
                .setResourceOwnerAccount("test")
                .setResourceOwnerId(1L)
                .setSignName("test")
                .setRemark("test");
        // 复制代码运行请自行打印 API 的返回值
        client.addSmsSign(addSmsSignRequest);
    }
}
