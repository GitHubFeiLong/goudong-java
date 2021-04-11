package com.goudong.gateway.service;

import com.goudong.commons.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author msi
 * @Date 2021-04-08 16:30
 * @Version 1.0
 */
@Component
@FeignClient(value = "education-promote-oauth2-server")
public interface Oauth2Service {

    /**
     * 判断是否能访问
     * @param Authorization token字符串
     * @return
     */
    @GetMapping("/api/oauth2/open/authentication")
    Result authentication(@RequestHeader String Authorization, @RequestParam(required = true) String httpPath, @RequestParam(required = true) String httpMethod);

    @GetMapping("/api/oauth2/hello")
    Result hello();

    @GetMapping("/api/oauth2/demo/demo1")
    Result demo1();
}
