package com.goudong.user.controller.open;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.dto.user.BaseUser2CreateDTO;
import com.goudong.commons.dto.user.BaseUserByPhoneDTO;
import com.goudong.commons.dto.user.BaseUserDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.user.AccountRadioEnum;
import com.goudong.commons.exception.user.UserException;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.user.po.BaseUserPO;
import com.goudong.user.repository.BaseUserRepository;
import com.goudong.user.service.BaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    // @Resource
    // private AuthorityUserService authorityUserService;
    //
    // @Resource
    // private HttpServletRequest request;
    //
    // @Resource
    // private AuthorityUserUtil authorityUserUtil;

    /**
     * 用户持久层接口
     */
    private final BaseUserRepository baseUserRepository;

    /**
     * 用户服务层接口
     */
    private final BaseUserService baseUserService;

    /**
     * RedisTool
     */
    private final RedisTool redisTool;

    /**
     * 构造方法注入Bean
     */
    public OpenUerController(BaseUserRepository baseUserRepository,
                             BaseUserService baseUserService,
                             RedisTool redisTool
    ) {
        this.baseUserRepository = baseUserRepository;
        this.baseUserService = baseUserService;
        this.redisTool = redisTool;
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
    @Whitelist("根据手机号获取账号")
    public Result<BaseUserByPhoneDTO> getUserByPhone(@PathVariable String phone) {
        AssertUtil.isPhone(phone, "手机号码格式不正确，获取用户失败");
        BaseUserPO baseUserPO = baseUserRepository.findByPhone(phone);
        BaseUserByPhoneDTO baseUserByPhoneDTO = BeanUtil.copyProperties(baseUserPO, BaseUserByPhoneDTO.class);
        return Result.ofSuccess(baseUserByPhoneDTO);
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
    @Whitelist("检查用户名是否存在")
    public Result<List<String>> getUserByUsername(@PathVariable String username) {
        List<String> strings = baseUserService.generateUserName(username);
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
    @Whitelist("检查邮箱能否使用")
    public Result<Boolean> checkEmailInUse(@PathVariable String email) {
        AssertUtil.isEmail(email, "邮箱格式错误");

        BaseUserPO byEmail = baseUserRepository.findByEmail(email);

        Result<Boolean> result = Result.ofSuccess(byEmail==null);
        return result;
    }

    /**
     * 新增或修改用户
     * @param createDTO
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "创建普通账号", notes = "注册用户")
    @Whitelist("创建普通账号")
    public Result createUser(@RequestBody @Validated BaseUser2CreateDTO createDTO) {
        AssertUtil.isPhone(createDTO.getPhone(), "手机号格式错误");
        AssertUtil.isEnum(createDTO.getAccountRadio(), AccountRadioEnum.class);

        BaseUserDTO userDTO = BeanUtil.copyProperties(createDTO, BaseUserDTO.class);
        BaseUserDTO user = baseUserService.createUser(userDTO);

        return Result.ofSuccess(user);
    }

    /**
     * 根据登录用户名、手机号、邮箱 查询用户基本信息
     * @param loginName 用户名、手机号、邮箱
     * @return
     */
    @GetMapping("/info/{login-name}")
    @ApiOperation(value = "根据登录用户名获取用户信息", notes = "登陆用户名包括，用户名，邮箱及密码")
    @ApiImplicitParam(name = "login-name", value = "登陆用户名")
    @Whitelist("根据账户名查询基本信息")
    public Result getUserByLoginName(@PathVariable("login-name") String loginName){
        List<BaseUserPO> userByLoginName = baseUserService.getUserByLoginName(loginName);
        if (CollectionUtils.isEmpty(userByLoginName)) {
            throw new UserException(ClientExceptionEnum.NOT_FOUND, "用户不存在");
        }
        BaseUserDTO baseUserDTO = BeanUtil.copyProperties(userByLoginName.get(0), BaseUserDTO.class, "password");
        return Result.ofSuccess(baseUserDTO);
    }

    // /**
    //  * 修改用户密码
    //  * @param user2UpdatePasswordDTO
    //  * @return
    //  */
    // @PatchMapping("/password")
    // @ApiOperation(value = "修改密码")
    // @IgnoreResource("修改密码")
    // public Result<AuthorityUserVO> updatePassword(@RequestBody @Validated BaseUser2UpdatePasswordDTO user2UpdatePasswordDTO){
    //     BaseUserDTO baseUserDTO = BeanUtil.copyProperties(user2UpdatePasswordDTO, BaseUserDTO.class);
    //     AuthorityUserDTO var1 = authorityUserService.updatePassword(baseUserDTO);
    //
    //     return Result.ofSuccess(BeanUtil.copyProperties(var1, AuthorityUserVO.class));
    // }
    //
    // /**
    //  * 绑定openId
    //  * @param updateOpenIdVO
    //  * @return
    //  */
    // @PatchMapping("/bind-open-id")
    // @ApiOperation(value = "绑定openId", hidden = true)
    // public Result updateOpenId(@RequestBody @Validated AuthorityUser2UpdateOpenIdVO updateOpenIdVO){
    //     AuthorityUserDTO userDTO = BeanUtil.copyProperties(updateOpenIdVO, AuthorityUserDTO.class);
    //
    //     AuthorityUserDTO userDTO1 = authorityUserService.updateOpenId(userDTO);
    //     AuthorityUserVO userVO = BeanUtil.copyProperties(userDTO1, AuthorityUserVO.class);
    //     return Result.ofSuccess(userVO);
    // }
    //
    // /**
    //  * 查询用户的详细信息
    //  * @param loginName 用户名/手机号/邮箱
    //  * @return
    //  */
    // @GetMapping("/detail-info/{login-name}")
    // @ApiOperation(value = "查询用户的详细信息")
    // @ApiImplicitParam(name = "login-name", value = "账户名")
    // public Result<AuthorityUserDTO> getUserDetailByLoginName (@PathVariable("login-name") String loginName){
    //     AuthorityUserDTO authorityUserDTO = authorityUserService.getUserDetailByLoginName(loginName);
    //     return Result.ofSuccess(authorityUserDTO);
    // }
}
