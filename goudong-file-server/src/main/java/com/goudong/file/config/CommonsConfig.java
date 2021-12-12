package com.goudong.file.config;

import com.goudong.commons.aop.LoggingAop;
import com.goudong.commons.aop.RepeatAop;
import com.goudong.commons.core.mvc.error.ErrorAttributes;
import com.goudong.commons.core.mvc.error.ErrorController;
import com.goudong.commons.utils.AuthorityUserUtil;
import com.goudong.commons.core.redis.RedisOperationsUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * commons模块的ben注入
 * TODO ApplicationRunnerConfig
 * @author msi
 * @version 1.0
 * @date 2021/12/11 17:42
 */
// @Configuration
public class CommonsConfig {
    private final HttpServletRequest request;

    public CommonsConfig(HttpServletRequest request) {
        this.request = request;
    }

    /*
    ==========================================================
    ===========================Aop===============================
    ==========================================================
     */
    /**
     * 日志切面
     * @param environment
     * @return
     */
    @Bean
    public LoggingAop loggingAop(Environment environment) {
        return new LoggingAop(environment);
    }

    /**
     * 防止重复请求
     * @return
     */
    @Bean
    @ConditionalOnClass(value = {RedisOperationsUtil.class, AuthorityUserUtil.class})
    public RepeatAop repeatAop() {
        return new RepeatAop();
    }

    /**
     * 自定义异常逻辑，返回自定义格式的json错误信息
     * @return
     */
    @Bean
    public ErrorAttributes errorAttributes () {
        return new ErrorAttributes(request);
    }

    @Bean
    public ErrorController ErrorController(ErrorAttributes errorAttributes){
        return new ErrorController(errorAttributes);
    }
}
