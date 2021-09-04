package com.goudong.oauth2;

import com.goudong.commons.constant.BasePackageConst;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * http://localhost:10003/oauth/authorize?response_type=code&client_id=admin&redirect_uri=http://www.baidu.com&scope=all&state=normal
 * 教程：https://juejin.cn/post/6844903987137740813
 * 教程：https://blog.csdn.net/bluuusea/article/details/80284458?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.essearch_pc_relevant&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.essearch_pc_relevant
 * 教程：https://cloud.tencent.com/developer/article/1450973
 * 访问上面的路径获取code
 * @Author msi
 * @Date 2021-05-25 14:06
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {BasePackageConst.OAUTH2, BasePackageConst.COMMONS})
@EnableAuthorizationServer
@EnableResourceServer
@EnableDiscoveryClient
@EnableFeignClients(basePackages = BasePackageConst.OPENFEIGN)
@MapperScan(basePackages = {BasePackageConst.OAUTH2_MAPPER, BasePackageConst.COMMONS_MAPPER})
public class Oauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
