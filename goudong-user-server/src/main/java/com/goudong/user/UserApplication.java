package com.goudong.user;

import com.goudong.commons.constant.BasePackageConst;
import com.goudong.commons.utils.LogUtil;
import com.goudong.user.config.UIProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StopWatch;

/**
 * 类描述：
 * 注解 @EnableConfigurationProperties 开启配置文件映射对象属性（自动转驼峰）
 * @Author msi
 * @Date 2021/2/9 19:20
 * @Vsion 1.0
 */
@EnableConfigurationProperties({UIProperties.class})
@SpringBootApplication(scanBasePackages = {BasePackageConst.USER, BasePackageConst.COMMONS/*, BasePackageConst.SECURITY*/})
@MapperScan(basePackages = {BasePackageConst.USER_MAPPER, BasePackageConst.COMMONS_MAPPER})
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients(basePackages = BasePackageConst.OPENFEIGN)
@Slf4j
public class UserApplication {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(UserApplication.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
        stopWatch.stop();

        // 获取环境变量
        Environment environment = context.getBean(Environment.class);
        Integer port = environment.getProperty("server.port", Integer.class);
        String contextPath = environment.getProperty("server.servlet.context-path");
        String applicationName = environment.getProperty("spring.application.name");



        LogUtil.info(log, "{} 服务启动完成，耗时:{}s，服务访问地址: http://127.0.0.1:{}{}/doc.html ",
                applicationName,
                stopWatch.getTotalTimeSeconds(),
                port,
                contextPath);

        System.out.println("\033[30;4m--------------------------\033[0m");
        System.out.println("http://127.0.0.1:{}{}/doc.html");
        System.out.println("--------------------------");
    }

}
