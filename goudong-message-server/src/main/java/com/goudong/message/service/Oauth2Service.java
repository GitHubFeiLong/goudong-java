package com.goudong.message.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author msi
 * @Date 2021-05-10 10:53
 * @Version 1.0
 */
@FeignClient(name="nacos-discovery-server")
public interface Oauth2Service {
    @GetMapping("/hello")
    String hello(@RequestParam String name);
}
