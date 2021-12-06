package com.goudong.gateway;

import com.goudong.commons.config.LogApplicationStartup;
import com.goudong.commons.constant.BasePackageConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StopWatch;

import java.util.stream.Collectors;

/**
 * 网关模块
 * @Author msi
 * @Date 2021-04-08 14:05
 * @Version 1.0
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = BasePackageConst.OPENFEIGN)
@SpringBootApplication(
        scanBasePackages = {BasePackageConst.GATEWAY}
        , exclude = {DataSourceAutoConfiguration.class}
)
@Slf4j
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

    /**
     * openfeign 需要HTTP MessageConverters
     * @param converters
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
}
