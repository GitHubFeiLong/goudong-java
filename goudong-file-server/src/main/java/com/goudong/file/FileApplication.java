package com.goudong.file;

import com.goudong.commons.annotation.enable.EnableCommonsGlobalExceptionHandler;
import com.goudong.commons.annotation.enable.EnableCommonsRedisConfig;
import com.goudong.commons.annotation.enable.EnableCommonsWebMvcConfig;
import com.goudong.commons.core.LogApplicationStartup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StopWatch;


/**
 * 类描述：
 * 文件服务
 * @author msi
 * @date 2021/12/1 19:46
 * @version 1.0
 */
@Slf4j
@SpringBootApplication
@EnableCommonsRedisConfig
@EnableCommonsWebMvcConfig
@EnableCommonsGlobalExceptionHandler
// @EnableCommonsJpaConfig
@EnableJpaAuditing()
@EntityScan("com.goudong.file.po")
@EnableJpaRepositories(basePackages = {"com.goudong.file.repository"})
public class FileApplication {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(FileApplication.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
        stopWatch.stop();

        // 获取环境变量
        Environment environment = context.getBean(Environment.class);

        LogApplicationStartup.logApplicationStartup(environment, (int)stopWatch.getTotalTimeSeconds());
    }

}
