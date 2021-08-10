package com.goudong.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * 类描述：
 * 配置全局异常
 * @Author e-Feilong.Chen
 * @Date 2021/8/10 15:56
 */
@Configuration
public class GlobalGatewayExceptionConfig {
    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                             ServerCodecConfigurer serverCodecConfigurer){
        GlobalGatewayExceptionHandler globalGatewayExceptionHandler =new GlobalGatewayExceptionHandler();
        globalGatewayExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        globalGatewayExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        globalGatewayExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return globalGatewayExceptionHandler;
    }
}
