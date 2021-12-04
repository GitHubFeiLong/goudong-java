package com.goudong.file;

import com.goudong.commons.config.LogApplicationStartup;
import com.goudong.commons.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StopWatch;


/**
 * 类描述：
 * 文件服务，对开源项目进行二次开发
 * @see https://kkfileview.keking.cn/zh-cn/index.html
 * @author msi
 * @date 2021/12/1 19:46
 * @version 1.0
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
public class FileApplication {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(FileApplication.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                // .banner(new AppBanner())
                .run(args);
        stopWatch.stop();

        Integer port = context.getBean(ServerProperties.class).getPort();
        LogUtil.info(log, "goudong-file-server 服务启动完成，耗时:{}s，服务访问地址: http://127.0.0.1:{} ", stopWatch.getTotalTimeSeconds(), port);

        LogApplicationStartup.logApplicationStartup(context.getEnvironment());
    }

}
