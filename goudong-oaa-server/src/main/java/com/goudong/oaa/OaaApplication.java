package com.goudong.oaa;

import com.goudong.commons.constant.BasePackageConst;
import com.goudong.commons.constant.CommonConst;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://localhost:10003/oauth/authorize?response_type=code&client_id=admin&redirect_uri=http://www.baidu.com&scope=all&state=normal
 * 教程：https://juejin.cn/post/6844903987137740813
 * 访问上面的路径获取code
 * @Author msi
 * @Date 2021-05-25 14:06
 * @Version 1.0
 */
@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.goudong.oaa.mapper"})
public class OaaApplication {
    public static void main(String[] args) {
        SpringApplication.run(OaaApplication.class, args);
    }
}
