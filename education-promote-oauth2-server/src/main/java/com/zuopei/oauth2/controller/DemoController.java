package com.zuopei.oauth2.controller;

import com.zuopei.commons.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author msi
 * @Date 2021-04-08 17:22
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/oauth2/demo")
public class DemoController {

    @GetMapping("/demo1")
    public Result demo1 () throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        return Result.ofSuccess("进入搜保护的资源");
    }

}
