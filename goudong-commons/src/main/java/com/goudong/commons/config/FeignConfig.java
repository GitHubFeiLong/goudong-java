package com.goudong.commons.config;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.constant.core.BasePackageConst;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.enumerate.core.ServerExceptionEnum;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.exception.ServerException;
import com.goudong.commons.framework.core.Result;
import feign.*;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 类描述：
 * Feigin配置
 * @Author msi
 * @Date 2021-08-21 9:12
 * @Version 1.0
 */
@Configuration
@EnableFeignClients(basePackages = {BasePackageConst.OPENFEIGN})
public class FeignConfig {

    /**
     * gateway报错
     * openfeign 需要HTTP MessageConverters
     * @param converters
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    /**
     * 日志级别，生产需要调到info及以上
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }


    /**
     * 使用feign调用远程服务时,会先拦截请求,填充请求头信息
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //1.RequestContextHolder拿到当前请求的数据，相当与拿到controller入参的HttpServletRequest
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null) {
                    //老请求
                    HttpServletRequest request = requestAttributes.getRequest();

                    //2.同步请求头信息->Authorization,Aes-Key
                    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
                    requestTemplate
                            .header(HttpHeaders.AUTHORIZATION, token)
                            // 自定义内部服务用户信息传递
                            .header(HttpHeaderConst.REQUEST_USER, request.getHeader(HttpHeaderConst.REQUEST_USER))
                            // 自定义Aes-Key密钥
                            .header(HttpHeaderConst.AES_KEY, request.getHeader(HttpHeaderConst.AES_KEY))
                    ;

                }

            }
        };
    }

    /**
     * 将微服务的异常信息解码，然后响应原异常。
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder() {
            @Override
            public Exception decode(String s, Response response) {
                try {
                    Reader reader = response.body().asReader(StandardCharsets.UTF_8);
                    String body = Util.toString(reader);
                    Result result = JSON.parseObject(body, Result.class);
                    if (result.getCode() == null) {
                        return new BasicException(response.status(), String.valueOf(response.status()), ServerExceptionEnum.SERVER_ERROR.getClientMessage(), body);
                    }
                    return new BasicException(response.status(), result.getCode(), result.getClientMessage(), result.getServerMessage());
                } catch (Exception e) {
                    return ServerException.serverException(ServerExceptionEnum.SERVER_ERROR, e.getMessage());
                }
            }
        };
    }
}
