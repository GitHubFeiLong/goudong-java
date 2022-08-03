package com.goudong.commons.autoconfigure;

import com.goudong.commons.properties.XxlJobProperties;
import com.goudong.commons.utils.core.LogUtil;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * xxl-job 自动配置
 * @author cfl
 * @version 1.0
 * @date 2022/7/13 21:06
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = "xxl.job", name = "enable", havingValue = "true", matchIfMissing = false)
public class XxlJobAutoConfiguration {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    @Bean
    @ConditionalOnMissingBean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties xxlJobProperties) {
        LogUtil.debug(log, "初始化xxl-job");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getExecutor().getAppName());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getExecutor().getPort());
        /*
            因为多网卡的原因，需要将地址手动在xxl-job-admin中注册，这样才能成功
            http://192.168.31.136:9999/
         */
        xxlJobSpringExecutor.setAddress(xxlJobProperties.getExecutor().getAddress());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getExecutor().getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogRetentionDays());
        return xxlJobSpringExecutor;
    }
}