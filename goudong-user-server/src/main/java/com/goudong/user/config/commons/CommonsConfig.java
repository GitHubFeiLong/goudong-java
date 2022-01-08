package com.goudong.user.config.commons;

import com.goudong.commons.config.ApplicationRunnerConfig;
import com.goudong.commons.frame.mvc.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;


/**
 * 类描述：
 * Commons中的配置进行注入
 * @author msi
 * @version 1.0
 * @date 2021/12/11 9:59
 */
@Configuration
public class CommonsConfig {

    private final HttpServletRequest request;

    public CommonsConfig(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 自定义异常逻辑，返回自定义格式的json错误信息
     * @return
     */
    @Bean
    public ErrorAttributes errorAttributes () {
        return new ErrorAttributes(request);
    }

    /**
     * 数据库文档生成
     * @param environment
     * @return
     */
    // @Bean
    // @ConditionalOnClass(value = {CommonsIgnoreResourceService.class, CommonsAuthorityMenuService.class, JobQuartzManager.class})
    public ApplicationRunnerConfig applicationRunnerConfig(Environment environment) {
        return new ApplicationRunnerConfig();
    }

}
