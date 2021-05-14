package com.goudong.oauth2.controller.open;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.vo.AuthorityUser2CreateVO;
import com.goudong.commons.vo.AuthorityUserVO;
import com.goudong.oauth2.service.AuthorityUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 类描述：
 * 用户相关开放接口
 * @Author msi
 * @Date 2021-05-13 19:38
 * @Version 1.0
 */
@Api(tags = "用户开放接口")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/oauth2/open/user")
public class OpenUerController {

    @Resource
    private AuthorityUserService authorityUserService;


    @GetMapping("/demo")
    public Result demo () {
        BasicException.ClientException.resourceNotFound("用户:" + "demo" + "不存在");
        throw new RuntimeException("异常");
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
    })
    public Result login (String username, String password) {
        log.info("123123");
        return Result.ofSuccess();
    }


    /**
     * 根据手机号获取账号
     *
     * @param phone
     * @return
     */
    @GetMapping("/phone/{phone}")
    @ApiOperation(value = "根据手机号获取账号")
    @ApiImplicitParam(name = "phone", value = "手机号")
    public Result<AuthorityUserVO> getUserByPhone(@PathVariable String phone) {
        AssertUtil.isPhone(phone, "手机号码格式不正确，获取用户失败");
        List<AuthorityUserDTO> authorityUserDTOS = authorityUserService.listByAuthorityUserDTO(AuthorityUserDTO.builder().phone(phone).build());

        AuthorityUserVO authorityUserVO = authorityUserDTOS.isEmpty() ? null : (AuthorityUserVO)BeanUtil.copyProperties(authorityUserDTOS.get(0), AuthorityUserVO.class);
        return Result.ofSuccess(authorityUserVO);
    }

    /**
     * 检查用户名是否存在，存在就返回三条可使用的账号名
     *
     * @param username
     * @return
     */
    @GetMapping("/check-username/{username}")
    @ApiOperation(value = "检查用户名是否存在", notes = "注册时，检查用户名是否可用。可用时，返回空集合；\n当不可用时，返回3个可用的用户名")
    @ApiImplicitParam(name = "username", value = "用户名")
    public Result<List<String>> getUserByUsername(@PathVariable @NotBlank(message = "用户名不能为空") String username) {
        List<String> strings = authorityUserService.generateUserName(username);
        return Result.ofSuccess(strings);
    }

    /**
     * 检查邮箱是否能使用，true 表示可以使用
     *
     * @param email
     * @return
     */
    @GetMapping("/check-email/{email}")
    @ApiOperation(value = "检查邮箱能否使用", notes = "当返回对象的data属性为True时，表示可以使用；当邮箱不可以使用时，使用 dataMap.status 进行判断：0 邮箱不正确；1 邮箱正确")
    @ApiImplicitParam(name = "email", value = "邮箱")
    public Result<Boolean> checkEmailInUse(@PathVariable String email) {
        AssertUtil.isEmail(email, "邮箱格式错误");
        List<AuthorityUserDTO> authorityUserDTOS = authorityUserService.listByAuthorityUserDTO(AuthorityUserDTO.builder().email(email).build());
        Result<Boolean> result = Result.ofSuccess(authorityUserDTOS.isEmpty());
        return result;
    }

    /**
     * 新增或修改用户
     * @param createVO
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "创建普通账号", notes = "注册用户")
    public Result createUser(@RequestBody @Validated AuthorityUser2CreateVO createVO) {
        AssertUtil.isPhone(createVO.getPhone(), "手机号格式错误");

        AuthorityUserDTO userDTO = (AuthorityUserDTO)BeanUtil.copyProperties(createVO, AuthorityUserDTO.class);
        AuthorityUserDTO user = authorityUserService.createUser(userDTO);
        AuthorityUserVO authorityUserVO = (AuthorityUserVO) BeanUtil.copyProperties(user, AuthorityUserVO.class);
        return Result.ofSuccess(authorityUserVO);
    }

}
