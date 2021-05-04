package com.goudong.oauth2.controller.open;

import com.goudong.commons.pojo.Result;
import com.goudong.oauth2.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 类描述：
 * 用户控制器
 * @Author msi
 * @Date 2021-05-02 13:33
 * @Version 1.0
 */
@Api(tags = "用户")
@Slf4j
@Controller
@RequestMapping("/oauth/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/user")
    @ApiOperation("注册账号")
    public Result register() {
        log.info("hhh");

        return Result.ofSuccess();
    }
    // 修改密码
    // 绑定qq
    // 登录

}
