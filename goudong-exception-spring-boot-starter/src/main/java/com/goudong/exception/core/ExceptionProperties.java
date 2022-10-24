package com.goudong.exception.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/24 9:10
 */
@ConfigurationProperties(prefix = "commons.goudong.exception")
public class ExceptionProperties {

    /**
     * 是启用exception
     */
    private boolean enable = true;
}
