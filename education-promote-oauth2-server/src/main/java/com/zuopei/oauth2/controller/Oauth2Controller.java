package com.zuopei.oauth2.controller;

import com.alibaba.fastjson.JSON;
import com.zuopei.commons.pojo.Result;
import com.zuopei.commons.validated.Create;
import com.zuopei.oauth2.entity.AuthorityUserDO;
import com.zuopei.oauth2.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 *
 * @ClassName UserController
 * @Author msi
 * @Date 2021/2/9 18:11
 * @Version 1.0
 */
@Api(value = "用户", tags = "用户")
@Slf4j
@RestController
@RequestMapping("/api/oauth2")
public class Oauth2Controller {


    @GetMapping("/hello")
    public Result<String> hello () {
        log.info("haha");
        JSON.toJSONString("hello");
        return Result.ofSuccess("hello world");
    }

    @PostMapping("/user")
    @ApiOperation(value = "创建用户", notes = "注册用户")
    public Result createUser(@RequestBody @Validated({Create.class}) AuthorityUserDO authorityUser){

        return Result.ofSuccess(authorityUser);
    }

    /**
     * 获取新的token
     * @param request
     * @return
     */
    @GetMapping("/token")
    @ApiOperation(value = "获取新的token")
    public Result createToken(HttpServletRequest request) {
        // 请求头中的token字符串（包含 Bearer）
        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        // 去掉前面的 "Bearer " 字符串
        String token = tokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
        // 解析token为对象
        AuthorityUserDO authorityUserDO = JwtTokenUtil.resolveToken(token);

        // 短期有效
        String shortToken = JwtTokenUtil.generateToken(authorityUserDO, JwtTokenUtil.VALID_SHORT_TERM_HOUR);
        // 长期有效
        String longToken = JwtTokenUtil.generateToken(authorityUserDO, JwtTokenUtil.VALID_LONG_TERM_HOUR);

        // 返回对象
        Map<String, String> map = new HashMap();
        map.put(JwtTokenUtil.TOKEN, shortToken);
        map.put(JwtTokenUtil.REFRESH_TOKEN, longToken);

        return Result.ofSuccess(map);
    }



}
