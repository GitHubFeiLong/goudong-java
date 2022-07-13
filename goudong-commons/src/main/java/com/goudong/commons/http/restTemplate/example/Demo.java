package com.goudong.commons.http.restTemplate.example;

import com.goudong.commons.http.restTemplate.config.BeanConfig;
import com.goudong.commons.framework.core.Result;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

        // String url = "http://localhost:10001" + ApiInfoEnum.CONTROLLER__METHOD.getApi();
        //String url = "http://localhost:8510/api/icc/asset-auth-period-job/test";
        String url = "http://127.0.0.1:10001/api/user/base-user/check-registry/phone/15213507716";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.apache.kylin-v4-public+json");
        headers.add("Accept-Language", "en");
        headers.add("Content-Type", "application/json;charset=utf-8");
        headers.add("Authorization", "Basic Q0xVRUNFTlRFUl9VQVRfUVVFUlk6Q2FDbHVlQ2VudGVyIzA2MTU=");
        HttpEntity httpEntity = new HttpEntity<>(null, headers);
        //Result forObject = restTemplate.getForObject(url, Result.class);
        ResponseEntity<Result> forEntity = restTemplate.getForEntity(url, Result.class);

        System.out.println("forEntity = " + forEntity);

    }
}
