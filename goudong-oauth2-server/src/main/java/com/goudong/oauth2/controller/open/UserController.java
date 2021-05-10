package com.goudong.oauth2.controller.open;

import com.google.common.collect.Lists;
import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.entity.InvalidEmailDO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.validated.Create;
import com.goudong.oauth2.service.InvalidEmailService;
import com.goudong.oauth2.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * 用户控制器
 *
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

    @Resource
    private InvalidEmailService invalidEmailService;

    /**
     * 根据手机号获取账号
     *
     * @param phone
     * @return
     */
    @GetMapping("/phone/{phone}")
    public Result<AuthorityUserDO> getUserByPhone(@PathVariable String phone) {
        return Result.ofSuccess(userService.getUserByPhone(phone));
    }

    /**
     * 检查用户名是否存在，存在就返回三条可使用的账号名
     *
     * @param username
     * @return
     */
    @GetMapping("/check-username/{username}")
    public Result<List<String>> getUserByUsername(@PathVariable @NotBlank(message = "用户名不能为空") String username) {
        List<String> strings = userService.generateUserName(username);
        return Result.ofSuccess(strings);
    }

    /**
     * 检查邮箱是否能使用，true 表示可以使用
     *
     * @param email
     * @return
     */
    @GetMapping("/check-email/{email}")
    public Result<Boolean> checkEmailInUse(@PathVariable String email) {
        AssertUtil.isEmail(email, "邮箱格式错误");
        InvalidEmailDO byEmail = invalidEmailService.getByEmail(email);
        // 额外信息，status：0 -> 无效； status：1 -> 有效，但被使用了
        Map<String, Integer> map = new HashMap<>();
        // 邮箱不能使用
        if (byEmail != null && !byEmail.getIsDelete()) {
            Result<Boolean> result = Result.ofSuccess(false);
            map.put("status", 0);
            result.setDataMap(map);
            return result;
        }
        AuthorityUserDO authorityUserDO = userService.getUserByEmail(email);
        Result<Boolean> result = Result.ofSuccess(authorityUserDO == null);
        map.put("status", 1);
        result.setDataMap(map);
        return result;
    }


    @PostMapping
    @ApiOperation("注册账号")
    public Result createUser(@RequestBody @Validated(Create.class) AuthorityUserDO authorityUserDO) {
        log.info(authorityUserDO.toString());
        return Result.ofSuccess(authorityUserDO);
    }
    // 修改密码
    // 绑定qq
    // 登录

}
