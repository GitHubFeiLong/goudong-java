package com.goudong.user.controller.open;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.goudong.commons.annotation.IgnoreResource;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.dto.user.BaseUserByPhoneDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.AuthorityUserUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.vo.AuthorityUser2CreateVO;
import com.goudong.commons.vo.AuthorityUser2UpdateOpenIdVO;
import com.goudong.commons.vo.AuthorityUser2UpdatePasswordVO;
import com.goudong.commons.vo.AuthorityUserVO;
import com.goudong.user.service.AuthorityUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private HttpServletRequest request;

    @Resource
    private AuthorityUserUtil authorityUserUtil;



    /**
     * 根据手机号获取账号
     *
     * @param phone
     * @return
     */
    @GetMapping("/phone/{phone}")
    @ApiOperation(value = "根据手机号获取账号")
    @ApiImplicitParam(name = "phone", value = "手机号")
    @IgnoreResource("根据手机号获取账号")
    public Result<BaseUserByPhoneDTO> getUserByPhone(@PathVariable String phone) {
        AssertUtil.isPhone(phone, "手机号码格式不正确，获取用户失败");

        AuthorityUserPO authorityUserPO = authorityUserService.getOne(lambdaQueryWrapper);
        AuthorityUserVO authorityUserVO = BeanUtil.copyProperties(authorityUserPO, AuthorityUserVO.class);
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
    @IgnoreResource("检查用户名是否存在")
    public Result<List<String>> getUserByUsername(@PathVariable String username) {
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
    @IgnoreResource("检查邮箱能否使用")
    public Result<Boolean> checkEmailInUse(@PathVariable String email) {
        AssertUtil.isEmail(email, "邮箱格式错误");
        LambdaQueryWrapper<AuthorityUserPO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(AuthorityUserPO::getEmail, email);
        AuthorityUserPO authorityUserPO = authorityUserService.getOne(lambdaQueryWrapper);

        Result<Boolean> result = Result.ofSuccess(authorityUserPO==null);
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
    @IgnoreResource("创建普通账号")
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
    @IgnoreResource("根据账户名查询基本信息")
    public Result getUserByLoginName(@PathVariable("login-name") String loginName){

        AuthorityUserDTO authorityUserDTO = authorityUserService.getUserByLoginName(loginName);
        if (authorityUserDTO == null) {
            throw ClientException.clientException(ClientExceptionEnum.NOT_FOUND, "用户不存在");
        }
        return Result.ofSuccess(authorityUserDTO);
    }

    /**
     * 修改用户密码
     * @param updatePasswordVO
     * @return
     */
    @PatchMapping("/password")
    @ApiOperation(value = "修改密码")
    @IgnoreResource("修改密码")
    public Result<AuthorityUserVO> updatePassword(@RequestBody @Validated AuthorityUser2UpdatePasswordVO updatePasswordVO){
        AuthorityUserDTO var0 = BeanUtil.copyProperties(updatePasswordVO, AuthorityUserDTO.class);
        AuthorityUserDTO var1 = authorityUserService.updatePassword(var0);

        return Result.ofSuccess(BeanUtil.copyProperties(var1, AuthorityUserVO.class));
    }

    /**
     * 绑定openId
     * @param updateOpenIdVO
     * @return
     */
    @PatchMapping("/bind-open-id")
    @ApiOperation(value = "绑定openId", hidden = true)
    public Result updateOpenId(@RequestBody @Validated AuthorityUser2UpdateOpenIdVO updateOpenIdVO){
        AuthorityUserDTO userDTO = BeanUtil.copyProperties(updateOpenIdVO, AuthorityUserDTO.class);

        AuthorityUserDTO userDTO1 = authorityUserService.updateOpenId(userDTO);
        AuthorityUserVO userVO = BeanUtil.copyProperties(userDTO1, AuthorityUserVO.class);
        return Result.ofSuccess(userVO);
    }

    /**
     * 查询用户的详细信息
     * @param loginName 用户名/手机号/邮箱
     * @return
     */
    @GetMapping("/detail-info/{login-name}")
    @ApiOperation(value = "查询用户的详细信息")
    @ApiImplicitParam(name = "login-name", value = "账户名")
    public Result<AuthorityUserDTO> getUserDetailByLoginName (@PathVariable("login-name") String loginName){
        AuthorityUserDTO authorityUserDTO = authorityUserService.getUserDetailByLoginName(loginName);
        return Result.ofSuccess(authorityUserDTO);
    }
}
