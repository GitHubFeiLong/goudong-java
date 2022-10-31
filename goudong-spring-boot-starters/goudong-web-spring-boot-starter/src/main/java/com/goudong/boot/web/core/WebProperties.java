package com.goudong.boot.web.core;

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
@ConfigurationProperties(prefix = "commons.goudong.web")
public class WebProperties {

    /**
     * 是否启用
     */
    private boolean enable = true;


}
