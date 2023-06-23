package com.goudong.user.controller.user;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.enumerate.user.AccountRadioEnum;
import com.goudong.core.context.GoudongContext;
import com.goudong.core.lang.PageResult;
import com.goudong.core.lang.RegexConst;
import com.goudong.core.lang.Result;
import com.goudong.core.util.AssertUtil;
import com.goudong.user.dto.*;
import com.goudong.user.po.BaseUserPO;
import com.goudong.user.repository.BaseUserRepository;
import com.goudong.user.service.BaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

/**
 * 类描述：
 * 用户相关接口
 * @author  msi
 * @Date 2021-05-13 19:38
 * @Version 1.0
 */
@Api(tags = "用户相关接口")
@Slf4j
@Validated
@RestController
@RequestMapping("/base-user")
public class BaseUerController {

    /**
     * 用户持久层接口
     */
    @Resource
    private BaseUserRepository baseUserRepository;

    /**
     * 用户服务层接口
     */
    @Resource
    private BaseUserService baseUserService;


    /**
     * 检查手机号是否可以被注册
     * 返回true可以使用
     * @param phone
     * @return
     */
    @GetMapping("/check-registry/phone/{phone}")
    @ApiOperation(value = "检查手机号", notes = "检查手机号是否可以使用，true可以使用")
    @ApiImplicitParam(name = "phone", value = "手机号")
    public Result<Boolean> checkPhone(@PathVariable String phone) {
        AssertUtil.isTrue(phone.matches(RegexConst.PHONE_LOOSE), "手机号码格式不正确");
        Long appId = GoudongContext.get().getAppId();
        BaseUserPO baseUserPO = baseUserRepository.findByAppIdAndPhone(appId, phone);
        Result<Boolean> booleanResult = Result.ofSuccess(baseUserPO == null);
        // 返回附加信息，用户基本信息
        if (baseUserPO != null) {
            HashMap<Object, Object> dataMap = new HashMap<>();
            dataMap.put("username", baseUserPO.getUsername());
            dataMap.put("validTime", baseUserPO.getValidTime());
            dataMap.put("email", baseUserPO.getEmail());
            booleanResult.setDataMap(dataMap);
        }

        return booleanResult;
    }

    /**
     * 检查用户名是否存在
     * 存在就返回三条可使用的账号名，不存在返回空数组
     * TODO 需要优化
     * @param username
     * @return
     */
    @GetMapping("/check-registry/username/{username}")
    @ApiOperation(value = "检查用户名是否存在", notes = "注册时，检查用户名是否可用。可用时，返回空集合；\n当不可用时，返回3个可用的用户名")
    @ApiImplicitParam(name = "username", value = "用户名")
    public Result<List<String>> checkUsername(@PathVariable String username) {
        List<String> strings = baseUserService.generateUserName(username);
        return Result.ofSuccess(strings);
    }

    /**
     * 检查邮箱是否能使用，
     * true 表示可以使用，false不可以使用（已经被人使用了）。
     * @param email
     * @return
     */
    @GetMapping("/check-registry/email/{email}")
    @ApiOperation(value = "检查邮箱能否使用", notes = "当返回对象的data属性为True时，表示可以使用")
    public Result<Boolean> checkEmail(@PathVariable String email) {
        AssertUtil.isEmail(email, "邮箱格式错误");
        Long appId = GoudongContext.get().getAppId();
        BaseUserPO byEmail = baseUserRepository.findByAppIdAndEmail(appId, email);

        Result<Boolean> result = Result.ofSuccess(byEmail==null);
        return result;
    }

    /**
     * 新增或修改用户
     * @param createDTO
     * @return
     */
    @PostMapping("/simple-create-user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "admin创建普通账号", notes = "后台手动创建单个用户")
    public Result<BaseUserDTO> simpleCreateUser(@RequestBody @Validated SimpleCreateUserReq createDTO) {
        BaseUserDTO baseUserDTO = baseUserService.simpleCreateUser(createDTO);
        return Result.ofSuccess(baseUserDTO);
    }

    /**
     * 新增用户
     * @param createDTO
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "ui注册账号")
    public Result<BaseUserDTO> createUser(@RequestBody @Validated BaseUser2CreateDTO createDTO) {
        AssertUtil.isTrue(createDTO.getPhone().matches(RegexConst.PHONE_LOOSE), "手机号格式错误");
        AssertUtil.isEmail(createDTO.getEmail(), "邮箱格式不正确");
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
    @GetMapping("/user-basic-info/{login-name}")
    @ApiOperation(value = "根据登录用户名获取用户信息")
    @ApiImplicitParam(name = "login-name", value = "登陆用户名")
    @Whitelist("根据账户名查询基本信息")
    public Result getUserByLoginName(@PathVariable("login-name") String loginName){
        List<BaseUserPO> userByLoginName = baseUserService.getUserByLoginName(loginName);
        AssertUtil.isNotEmpty(userByLoginName, () -> ClientException.client(ClientExceptionEnum.NOT_FOUND, "用户不存在"));
        BaseUserDTO baseUserDTO = BeanUtil.copyProperties(userByLoginName.get(0), BaseUserDTO.class, "password");
        return Result.ofSuccess(baseUserDTO);
    }

    /**
     * 修改用户密码
     * @param user2UpdatePasswordDTO
     * @return
     */
    @PatchMapping("/password")
    @ApiOperation(value = "修改密码")
    @Whitelist("修改密码")
    public Result<BaseUserDTO> updatePassword(@RequestBody @Validated BaseUser2UpdatePasswordDTO user2UpdatePasswordDTO){
        AssertUtil.isEquals(user2UpdatePasswordDTO.getId(), GoudongContext.get().getUserId(), () -> ClientException.clientByForbidden("无权修改其他账户密码"));
        BaseUserDTO baseUserDTO = BeanUtil.copyProperties(user2UpdatePasswordDTO, BaseUserDTO.class);
        return Result.ofSuccess(baseUserService.updatePassword(baseUserDTO));
    }

    // /**
    //  * 绑定openId
    //  * TODO
    //  * @param updateOpenIdDTO
    //  * @return
    //  */
    // @PatchMapping("/bind-open-id")
    // @ApiOperation(value = "绑定openId", hidden = true)
    // public Result updateOpenId(@RequestBody @Validated BaseUser2UpdateOpenIdDTO updateOpenIdDTO){
    //     AssertUtil.isEnum(updateOpenIdDTO.getUserType(), OtherUserTypeEnum.class);
    //
    //     BaseUserDTO userDTO = BeanUtil.copyProperties(updateOpenIdDTO, BaseUserDTO.class);
    //
    //     return Result.ofSuccess(baseUserService.updateOpenId(userDTO));
    // }

    /**
     * 查询用户的详细信息
     * @param loginName 用户名/手机号/邮箱
     * @return
     */
    @GetMapping("/detail-info/{login-name}")
    @ApiOperation(value = "查询用户的详细信息")
    @ApiImplicitParam(name = "login-name", value = "账户名")
    @Deprecated
    public Result<BaseUserDTO> getUserDetailByLoginName (@PathVariable("login-name") String loginName){
        BaseUserDTO authorityUserDTO = baseUserService.getUserDetailByLoginName(loginName);
        return Result.ofSuccess(authorityUserDTO);
    }

    @GetMapping("/page-field")
    @ApiOperation(value = "用户表的下拉分页查询")
    public Result<PageResult<BaseUserDTO>> pageByField (BaseUser2QueryPageDTO page){
        return Result.ofSuccess(baseUserService.pageByField(page));
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public Result<PageResult<BaseUserPageResp>> page (BaseUser2QueryPageDTO page){
        return Result.ofSuccess(baseUserService.page(page));
    }

    // @GetMapping("/{id}")
    // @ApiOperation(value = "查询用户信息")
    // public Result<BaseUserDTO> getUserById (@PathVariable Long id){
    //     return Result.ofSuccess(baseUserService.getUserById(id));
    // }

    @PutMapping("/admin/user")
    @ApiOperation(value = "admin-更新用户信息")
    public Result<BaseUserDTO> adminEditUser (@RequestBody @Validated AdminEditUserReq req){
        AssertUtil.isTrue(req.getId() > Integer.MAX_VALUE, () -> ClientException.clientByForbidden());
        return Result.ofSuccess(baseUserService.adminEditUser(req));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户")
    @ApiImplicitParam(name = "id", value = "user id", required = true)
    public Result<BaseUserDTO> deleteUserById (@PathVariable Long id){
        AssertUtil.isTrue(id > Integer.MAX_VALUE, () -> ClientException.clientByForbidden());
        BaseUserDTO userDTO = baseUserService.deleteUserById(id);
        return Result.ofSuccess(userDTO);
    }

    @DeleteMapping("/ids")
    @ApiOperation(value = "批量删除用户")
    @ApiImplicitParam(name = "id", value = "user id", required = true)
    public Result<Boolean> deleteUserById (@RequestParam(name = "ids")@NotNull @NotEmpty List<Long> ids){
        // 参数校验，预置用户不能删除
        AssertUtil.isFalse(ids.stream().filter(f -> f <= Integer.MAX_VALUE).findFirst().isPresent(), () -> ClientException.clientByForbidden());
        return Result.ofSuccess(baseUserService.deleteUserByIds(ids));
    }

    @PutMapping("/reset-password/{id}")
    @ApiOperation("重置用户密码")
    @ApiImplicitParam(name = "id", value = "user id", required = true)
    public Result<Boolean> resetPassword(@PathVariable Long id) {
        AssertUtil.isTrue(id > Integer.MAX_VALUE, () -> ClientException.clientByForbidden());
        return Result.ofSuccess(baseUserService.resetPassword(id));
    }

    @PutMapping("/change-enabled/{id}")
    @ApiOperation("修改激活状态")
    @ApiImplicitParam(name = "id", value = "user id", required = true)
    public Result<Boolean> changeEnabled(@PathVariable Long id) {
        AssertUtil.isTrue(id > Integer.MAX_VALUE, () -> ClientException.clientByForbidden());
        return Result.ofSuccess(baseUserService.changeEnabled(id));
    }

    @PutMapping("/change-locked/{id}")
    @ApiOperation("修改锁定状态")
    @ApiImplicitParam(name = "id", value = "user id", required = true)
    public Result<Boolean> changeLocked(@PathVariable Long id) {
        AssertUtil.isTrue(id > Integer.MAX_VALUE, () -> ClientException.clientByForbidden());
        return Result.ofSuccess(baseUserService.changeLocked(id));
    }
}
