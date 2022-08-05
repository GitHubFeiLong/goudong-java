package com.goudong.modules.xxljob;

import com.goudong.commons.annotation.enable.EnableCommonsXxlJobConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;

/**
 * 类描述：
 * xxl-job的基础使用教程
 * 官网:https://www.xuxueli.com/xxl-job
 * @author cfl
 * @version 1.0
 * @date 2022/8/4 20:21
 */
//@EnableCommonsXxlJobConfig
@SpringBootApplication
@EnableCommonsXxlJobConfig
public class XxlJobApplication {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(XxlJobApplication.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .run(args);

        // 获取环境变量
        Environment environment = context.getBean(Environment.class);
        stopWatch.stop();
        //LogApplicationStartup.logApplicationStartup(environment, (int)stopWatch.getTotalTimeSeconds());
    }
}
