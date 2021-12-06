package com.goudong.message;

import com.goudong.commons.config.LogApplicationStartup;
import com.goudong.commons.constant.BasePackageConst;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StopWatch;

/**
 * 类描述：
 * , scanBasePackageClasses = {GlobalExceptionHandler.class, LogAOP.class, RedisConfig.class}
 * 注解：
 *  @EnableDiscoveryClient 开启服务注册
 *  @EnableFeignClients 开启feign
 * @Author msi
 * @Date 2021-05-04 22:36
 * @Version 1.0
 */
@EnableFeignClients(basePackages = {BasePackageConst.OPENFEIGN})
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {BasePackageConst.MESSAGE, BasePackageConst.COMMONS})
@MapperScan(basePackages = {BasePackageConst.MESSAGE_MAPPER, BasePackageConst.COMMONS_MAPPER})
@EnableScheduling
@Slf4j
public class MessageApplication {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(MessageApplication.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
        stopWatch.stop();
        // 获取环境变量
        Environment environment = context.getBean(Environment.class);

        LogApplicationStartup.logApplicationStartup(context.getBean(Environment.class), (int)stopWatch.getTotalTimeSeconds());
    }
}
