package com.goudong.message;

import com.goudong.commons.constant.core.BasePackageConst;
import com.goudong.commons.framework.core.LogApplicationStartup;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StopWatch;

/**
 * 类描述：
 * 消息服务
 * @author msi
 * @date 2022/1/28 18:27
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {BasePackageConst.MESSAGE_MAPPER, BasePackageConst.COMMONS_MAPPER})
@EnableScheduling
@Slf4j
public class GoudongMessageServer {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(GoudongMessageServer.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
        stopWatch.stop();

        LogApplicationStartup.logApplicationStartup(context.getBean(Environment.class), (int)stopWatch.getTotalTimeSeconds());
    }
}
