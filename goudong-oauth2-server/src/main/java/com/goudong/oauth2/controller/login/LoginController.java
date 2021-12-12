package com.goudong.oauth2.controller.login;

import com.goudong.commons.annotation.IgnoreResource;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.core.redis.RedisOperationsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 登录和退出控制器（只是接口展示方便swagger上查看文档）
 * @Author msi
 * @Date 2021-05-02 13:33
 * @Version 1.0
 */
@Api(tags = "登录")
@Slf4j
@Validated
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private RedisOperationsUtil redisOperationsUtil;

    @Resource
    private HttpServletRequest httpServletRequest;

    @PostMapping("/login")
    @ApiOperation(value = "登录(password)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
    })
    @IgnoreResource("登录(password)")
    public Result login (String username, String password) {
        return Result.ofSuccess();
    }

    @PutMapping("/logout")
    @ApiOperation(value = "注销")
    @IgnoreResource("注销")
    public Result logout () {
        return Result.ofSuccess();
    }

    @PostMapping(value = "/token", headers = {JwtTokenUtil.TOKEN_HEADER})
    @ApiOperation(value = "登录(token)")
    @IgnoreResource("登录(token)")
    public Result login (@RequestHeader(JwtTokenUtil.TOKEN_HEADER) String token) {
        // 检查token是否有效
        AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);
        redisOperationsUtil.login(token, authorityUserDTO);
        return Result.ofSuccess(token);
    }





}
