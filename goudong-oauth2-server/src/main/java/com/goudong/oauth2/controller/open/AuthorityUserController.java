package com.goudong.oauth2.controller.open;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.vo.AuthorityUser2CreateVO;
import com.goudong.commons.vo.AuthorityUserVO;
import com.goudong.oauth2.service.AuthorityUserService;
import com.goudong.oauth2.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.net.URL;
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
public class AuthorityUserController {

    @Resource
    private AuthorityUserService authorityUserService;


    @PostMapping("/login")
    public Result login (String username, String password) {
        log.info("123123");
        return Result.ofSuccess();
    }
    @GetMapping("/demo")
    public Result demo () {
        ClassLoader classLoader = AuthorityUserController.class.getClassLoader();
        URL resource = classLoader.getResource(" org/apache/commons/codec/binary/Base64.class");
        log.info("Resource: {}", resource);
        return Result.ofSuccess(JwtTokenUtil.generateToken(new AuthorityUserDTO(), JwtTokenUtil.VALID_SHORT_TERM_HOUR));
    }

//    @GetMapping("/token")
//    @ApiOperation(value = "获取新的token")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public Result createToken(HttpServletRequest request, HttpServletResponse response) {
//        // 请求头中的token字符串（包含 Bearer）
//        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
//        // 去掉前面的 "Bearer " 字符串
//        String token = tokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
//        // 解析token为对象
//        AuthorityUserDTO authorityUserDO = JwtTokenUtil.resolveToken(token);
//
//        // 短期有效
//        String shortToken = JwtTokenUtil.generateToken(authorityUserDO, JwtTokenUtil.VALID_SHORT_TERM_HOUR);
//        // 长期有效
//        String longToken = JwtTokenUtil.generateToken(authorityUserDO, JwtTokenUtil.VALID_LONG_TERM_HOUR);
//
//        response.setHeader(JwtTokenUtil.TOKEN_HEADER, shortToken);
//        response.setHeader(JwtTokenUtil.REFRESH_TOKEN_HEADER, longToken);
//        // 返回对象
//        Map<String, String> map = new HashMap();
//        map.put(JwtTokenUtil.TOKEN_HEADER, shortToken);
//        map.put(JwtTokenUtil.REFRESH_TOKEN_HEADER, longToken);
//
//        return Result.ofSuccess(map);
//    }
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
        AuthorityUserDTO userDTO = (AuthorityUserDTO)BeanUtil.copyProperties(createVO, AuthorityUserDTO.class);
        AuthorityUserDTO user = authorityUserService.createUser(userDTO);
        AuthorityUserVO authorityUserVO = (AuthorityUserVO) BeanUtil.copyProperties(user, AuthorityUserVO.class);
        return Result.ofSuccess(authorityUserVO);
    }
    // 修改密码
    // 绑定qq
    // 登录

}
