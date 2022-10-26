package com.goudong.boot.exception.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/10/24 9:10
 */
@Data
@ConfigurationProperties(prefix = "commons.goudong.exception")
public class ExceptionProperties {

    /**
     * 是启用exception
     */
    private boolean enable = true;
}
