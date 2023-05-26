package com.goudong.boot.web.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * 配置扫描
 * @author cfl
 * @version 1.0
 * @date 2023/5/26 14:49
 */
@Configuration
@ConfigurationPropertiesScan(basePackages = {"com.goudong.boot.web.properties"})
public class PropertiesConfig {
}
