package com.goudong.oauth2.controller.wechat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 微信
 * @ClassName WeChatController
 * @Author msi
 * @Date 2021/2/27 13:31
 * @Version 1.0
 */
@Api(tags = "WeChat")
@Slf4j
@Controller
@RequestMapping("/oauth/we-chat")
public class WeChatController {

    @ApiOperation(value = "微信登录", notes = "")
    @GetMapping("/login")
    public void login (HttpServletRequest request, HttpServletResponse response) {
    }

    @ApiOperation(value = "微信登录回调", notes = "微信登录成功后的回调地址")
    @GetMapping("/fallback")
    public void fallback(HttpServletRequest request, HttpServletResponse response) {
    }

    @GetMapping("/demo1")
    @ResponseBody
    public String demo1 () {
        return "demo1";
    }
}
