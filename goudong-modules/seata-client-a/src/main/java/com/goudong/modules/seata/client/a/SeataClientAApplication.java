package com.goudong.modules.seata.client.a;

import com.goudong.commons.annotation.enable.EnableCommonsFeignConfig;
import com.goudong.commons.framework.core.LogApplicationStartup;
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
 *
 * @author cfl
 * @version 1.0
 * @date 2022/8/3 13:34
 */
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.goudong.modules.seata.client.a.po")
@EnableJpaRepositories(basePackages = {"com.goudong.modules.seata.client.a.repository"})
@EnableCommonsFeignConfig
public class SeataClientAApplication {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(SeataClientAApplication.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .run(args);

        // 获取环境变量
        Environment environment = context.getBean(Environment.class);
        stopWatch.stop();
        LogApplicationStartup.logApplicationStartup(environment, (int)stopWatch.getTotalTimeSeconds());
    }
}
