package com.goudong.oauth2.controller.open;

import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.pojo.Result;
import com.goudong.oauth2.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 类描述：
 * 用户控制器
 * @Author msi
 * @Date 2021-05-02 13:33
 * @Version 1.0
 */
@Api(tags = "用户")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/oauth2/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 根据手机号获取账号
     * @param phone
   @return
     */
    @GetMapping("/phone/{phone}")
    public Result<AuthorityUserDO> getUserByPhone(@PathVariable String phone) {
        AuthorityUserDO userByPhone = userService.getUserByPhone(phone);
        userByPhone.setPassword(null);
        return Result.ofSuccess(userByPhone);
    }



    @PostMapping("/user")
    @ApiOperation("注册账号")
    public Result register(@NotNull String name) {
        log.info("hhh");
        System.out.println("10 / 0 = " + 10 / 0);
        return Result.ofSuccess();
    }
    // 修改密码
    // 绑定qq
    // 登录

}
