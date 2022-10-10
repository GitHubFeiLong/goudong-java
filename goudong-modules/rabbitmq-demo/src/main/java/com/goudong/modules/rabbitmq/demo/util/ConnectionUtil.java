package com.goudong.modules.rabbitmq.demo.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类描述：
 * 连接工具类
 * @author cfl
 * @version 1.0
 * @date 2022/10/10 13:49
 */
public class ConnectionUtil {

    /**
     * 获取MQ的连接
     * @return
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        // 定义一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 设置服务地址
        factory.setHost("localhost");

        // AMQP 5672
        factory.setPort(5672);

        // vhost
        factory.setVirtualHost("/vhost01");

        // 用户名
        factory.setUsername("admin");

        // 密码
        factory.setPassword("123456");

        return factory.newConnection();
    }

}
