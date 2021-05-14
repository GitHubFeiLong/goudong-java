package com.goudong.oauth2.controller.open;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：
 * 用户控制器
 * @Author msi
 * @Date 2021-05-02 13:33
 * @Version 1.0
 */
@Api(tags = "登录后操作用户")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/oauth2/user")
public class AuthorityUserController {

}
