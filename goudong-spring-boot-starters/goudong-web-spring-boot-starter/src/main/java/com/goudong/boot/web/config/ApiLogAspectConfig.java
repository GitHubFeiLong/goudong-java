package com.goudong.boot.web.config;

import com.goudong.boot.web.bean.ApiLogAspectJExpressionPointcut;
import com.goudong.boot.web.bean.ApiLogInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：
 * 接口日志切面配置
 * @author cfl
 * @version 1.0
 * @date 2023/4/4 10:28
 */
@Configuration
public class ApiLogAspectConfig {

    @Value("${goudong.web.api-log.pointcut.expression:execution(public * com.goudong.*.controller..*.*(..))}")
    private String apiLogExpression;

    public ApiLogInterceptor getApiLogInterceptor() {
        ApiLogInterceptor apiLogInterceptor = new ApiLogInterceptor();
        return apiLogInterceptor;
    }

    /**
     * 配置 ApiLogInterceptor 的切点表达式
     * @return
     */
    public ApiLogAspectJExpressionPointcut getApiLogAspectJExpressionPointcut() {
        ApiLogAspectJExpressionPointcut apiLogAspectJExpressionPointcut = new ApiLogAspectJExpressionPointcut();
        apiLogAspectJExpressionPointcut.setExpression(apiLogExpression);
        return apiLogAspectJExpressionPointcut;
    }

    /**
     * 将切点和切面进行绑定
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"org.aspectj.lang.JoinPoint"})
    public DefaultPointcutAdvisor ApiLogAspectDefaultPointcutAdvisor() {
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setPointcut(getApiLogAspectJExpressionPointcut());
        defaultPointcutAdvisor.setAdvice(getApiLogInterceptor());

        return defaultPointcutAdvisor;
    }

}
