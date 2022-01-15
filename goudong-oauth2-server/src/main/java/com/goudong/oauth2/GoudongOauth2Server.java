package com.goudong.oauth2;

import com.goudong.commons.annotation.enable.*;
import com.goudong.commons.frame.core.LogApplicationStartup;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.util.StopWatch;

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
@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
@EnableDiscoveryClient
@EnableCommonsWebMvcConfig
@EnableCommonsGlobalExceptionHandler
@EnableCommonsRedisConfig
@EnableCommonsFeignConfig
@EnableCommonsJpaConfig
@EntityScan("com.goudong.oauth2.po")
@EnableJpaRepositories(basePackages = {"com.goudong.oauth2.repository"})
public class GoudongOauth2Server {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(GoudongOauth2Server.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
        stopWatch.stop();
        LogApplicationStartup.logApplicationStartup(context.getEnvironment(), (int) stopWatch.getTotalTimeSeconds());
    }
}
