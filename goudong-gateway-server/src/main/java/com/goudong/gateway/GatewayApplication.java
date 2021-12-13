package com.goudong.gateway;

import com.goudong.commons.annotation.enable.EnableCommonsFeignConfig;
import com.goudong.commons.annotation.enable.EnableCommonsRedisConfig;
import com.goudong.commons.core.LogApplicationStartup;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;

/**
 * 网关模块
 * @Author msi
 * @Date 2021-04-08 14:05
 * @Version 1.0
 */
@EnableDiscoveryClient
@EnableCommonsFeignConfig
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableCommonsRedisConfig
public class GatewayApplication {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(GatewayApplication.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
        stopWatch.stop();

        LogApplicationStartup.logApplicationStartup(context.getBean(Environment.class), (int)stopWatch.getTotalTimeSeconds());
    }
}
