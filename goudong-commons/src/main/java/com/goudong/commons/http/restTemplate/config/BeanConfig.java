package com.goudong.commons.http.restTemplate.config;

import com.goudong.commons.http.restTemplate.interceptor.RequestResponseLoggingInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/10/27 22:38
 */
public class BeanConfig {

    public static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(clientHttpRequestFactory()));
        // restTemplate.setMessageConverters(Collections.singletonList(mappingJacksonHttpMessageConverter()));

        restTemplate.setInterceptors( Collections.singletonList(new RequestResponseLoggingInterceptor()) );

        return restTemplate;
    }
}
