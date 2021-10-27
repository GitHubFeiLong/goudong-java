package com.goudong.commons.http.restTemplate.example;

import com.goudong.commons.http.restTemplate.config.BeanConfig;
import com.goudong.commons.http.restTemplate.enumerate.ApiInfoEnum;
import com.goudong.commons.pojo.Result;
import org.springframework.web.client.RestTemplate;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/10/27 22:22
 */
public class Demo {
    public static void main(String[] args) {
        RestTemplate restTemplate = BeanConfig.getRestTemplate();

        String url = "http://localhost:10001" + ApiInfoEnum.CONTROLLER__METHOD.getApi();
        Result forObject = restTemplate.getForObject(url, Result.class);
        System.out.println("forObject = " + forObject);
    }
}
