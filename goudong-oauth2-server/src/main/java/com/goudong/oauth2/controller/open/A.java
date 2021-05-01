package com.goudong.oauth2.controller.open;

import com.goudong.commons.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-05-01 12:01
 * @Version 1.0
 */
@RequestMapping("/api/oauth2")
@RestController
public class A {

    @GetMapping("/demo")
    public Result demo() {
        return Result.ofSuccess();
    }
}
