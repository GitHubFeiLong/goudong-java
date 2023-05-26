package com.goudong.wx.central.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.redis.EnableCommonsRedisConfig;
import com.goudong.boot.web.EnableCommonsWebMvcConfig;
import com.goudong.boot.web.aop.ApiLogAop;
import com.goudong.boot.web.properties.ApiLogProperties;
import com.goudong.commons.annotation.enable.EnableCommonsJacksonConfig;
import com.goudong.commons.aop.LoggingAop;
import com.goudong.commons.framework.core.LogApplicationStartup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/3/14 11:55
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ConfigurationPropertiesScan(value = {"com.goudong.wx.central.control.properties"})
// @ConfigurationPropertiesScan(value = {"com.goudong.wx.central.control.properties", "com.goudong.boot.web.properties"})
@EnableCommonsRedisConfig
@EnableCommonsJacksonConfig
@EnableCommonsWebMvcConfig
public class GoudongWxCentralControlServer {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = new SpringApplicationBuilder(GoudongWxCentralControlServer.class)
                .logStartupInfo(false)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);

        // 获取环境变量
        Environment environment = context.getBean(Environment.class);
        stopWatch.stop();
        LogApplicationStartup.logApplicationStartup(environment, (int)stopWatch.getTotalTimeSeconds());
    }

    @Bean
    LoggingAop loggingAop(Environment environment, ObjectMapper objectMapper) {
        return new LoggingAop(environment, objectMapper);
    }

    /**
     * 接口日志切面
     * @param environment
     * @return
     */
    @Bean
    public ApiLogAop apiLogAop(Environment environment, ObjectMapper objectMapper, ApiLogProperties apiLogProperties) {
        return new ApiLogAop(environment, objectMapper, apiLogProperties);
    }
}
