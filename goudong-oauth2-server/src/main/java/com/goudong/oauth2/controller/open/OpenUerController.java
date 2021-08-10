package com.goudong.oauth2.controller.open;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.vo.AuthorityUser2CreateVO;
import com.goudong.commons.vo.AuthorityUser2UpdateOpenIdVO;
import com.goudong.commons.vo.AuthorityUser2UpdatePasswordVO;
import com.goudong.commons.vo.AuthorityUserVO;
import com.goudong.oauth2.service.AuthorityUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@RequestMapping("/open/user")
public class OpenUerController {

    @Resource
    private AuthorityUserService authorityUserService;

    @GetMapping("/demo")
    @ApiOperation("测试")
    public Result demo () {
        BasicException.exception(ClientExceptionEnum.UNAUTHORIZED);
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
        List<AuthorityUserDTO> authorityUserDTOS = authorityUserService.listByAndAuthorityUserDTO(AuthorityUserDTO.builder().phone(phone).build());

        AuthorityUserVO authorityUserVO = authorityUserDTOS.isEmpty() ? null : BeanUtil.copyProperties(authorityUserDTOS.get(0), AuthorityUserVO.class);
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
        List<AuthorityUserDTO> authorityUserDTOS = authorityUserService.listByAndAuthorityUserDTO(AuthorityUserDTO.builder().email(email).build());
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

        AuthorityUserDTO userDTO = BeanUtil.copyProperties(createVO, AuthorityUserDTO.class);
        AuthorityUserDTO user = authorityUserService.createUser(userDTO);
        AuthorityUserVO authorityUserVO = BeanUtil.copyProperties(user, AuthorityUserVO.class);
        return Result.ofSuccess(authorityUserVO);
    }

    /**
     * 根据登录用户名、手机号、邮箱 查询用户基本信息
     * @param loginName 用户名、手机号、邮箱
     * @return
     */
    @GetMapping("/info/{login-name}")
    @ApiOperation(value = "根据登录用户名获取用户信息", notes = "登陆用户名包括，用户名，邮箱及密码")
    @ApiImplicitParam(name = "login-name", value = "登陆用户名")
    public Result getUserByLoginName(@PathVariable("login-name") String loginName){
        AuthorityUserDTO build = AuthorityUserDTO.builder().username(loginName).phone(loginName).email(loginName).build();
        List<AuthorityUserDTO> authorityUserDTOS = authorityUserService.listByOrAuthorityUserDTO(build);
        if (authorityUserDTOS.isEmpty()) {
            return Result.ofFail(ClientException.resourceNotFound("用户不存在"));
        }
        return Result.ofSuccess(authorityUserDTOS.get(0));
    }

    /**
     * 修改用户密码
     * @param updatePasswordVO
     * @return
     */
    @PatchMapping("/password")
    public Result updatePassword(@RequestBody @Validated AuthorityUser2UpdatePasswordVO updatePasswordVO){
        AuthorityUserDTO userDTO = BeanUtil.copyProperties(updatePasswordVO, AuthorityUserDTO.class);
        userDTO =  authorityUserService.updateByPatch(userDTO);

        return Result.ofSuccess(userDTO);
    }

    /**
     * 绑定openId
     * @param updateOpenIdVO
     * @return
     */
    @PatchMapping("/bind-open-id")
    public Result updateOpenId(@RequestBody @Validated AuthorityUser2UpdateOpenIdVO updateOpenIdVO){
        AuthorityUserDTO userDTO = BeanUtil.copyProperties(updateOpenIdVO, AuthorityUserDTO.class);

        AuthorityUserDTO userDTO1 = authorityUserService.updateOpenId(userDTO);
        AuthorityUserVO userVO = BeanUtil.copyProperties(userDTO1, AuthorityUserVO.class);
        return Result.ofSuccess(userVO);
    }
}
