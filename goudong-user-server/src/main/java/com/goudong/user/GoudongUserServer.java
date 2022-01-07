package com.goudong.user;

import com.goudong.commons.annotation.enable.EnableCommonsFeignConfig;
import com.goudong.commons.annotation.enable.EnableCommonsJpaConfig;
import com.goudong.commons.annotation.enable.EnableCommonsMybatisPlusConfig;
import com.goudong.commons.core.LogApplicationStartup;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StopWatch;
/**
 * 类描述：
 * 注解 @EnableConfigurationProperties 开启配置文件映射对象属性（自动转驼峰）
 * @Author msi
 * @Date 2021/2/9 19:20
 * @Vsion 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCommonsFeignConfig
@EnableCommonsMybatisPlusConfig
@EnableCommonsJpaConfig
@EntityScan("com.goudong.user.po")
@EnableJpaRepositories(basePackages = {"com.goudong.user.repository"})
public class GoudongUserServer {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(GoudongUserServer.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);

        // 获取环境变量
        Environment environment = context.getBean(Environment.class);
        stopWatch.stop();
        LogApplicationStartup.logApplicationStartup(environment, (int)stopWatch.getTotalTimeSeconds());
    }

}
