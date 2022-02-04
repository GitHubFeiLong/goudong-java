package com.goudong.oauth2.controller.authentication;

import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.constant.core.HttpMethodConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.service.BaseMenuService;
import com.goudong.oauth2.service.BaseTokenService;
import com.goudong.oauth2.service.BaseWhitelistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 类描述：
 * 认证相关的接口
 * @Author msi
 * @Date 2021-05-02 13:33
 * @Version 1.0
 */
@Api(tags = "登录")
@Slf4j
@Validated
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    /**
     * 白名单服务层接口
     */
    private final BaseWhitelistService baseWhitelistService;

    /**
     * 菜单服务层接口
     */
    private final BaseMenuService baseMenuService;

    /**
     * 令牌服务层接口
     */
    private final BaseTokenService baseTokenService;

    public AuthenticationController(BaseWhitelistService baseWhitelistService,
                                    BaseMenuService baseMenuService,
                                    BaseTokenService baseTokenService) {
        this.baseWhitelistService = baseWhitelistService;
        this.baseMenuService = baseMenuService;
        this.baseTokenService = baseTokenService;
    }

    /**
     * 登录接口
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录(password)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
    })
    @Whitelist("登录接口")
    public Result login (String username, String password) {
        return Result.ofSuccess();
    }

    /**
     * 注销接口
     * @return
     */
    @PutMapping("/logout")
    @ApiOperation(value = "注销")
    public Result logout () {
        return Result.ofSuccess();
    }

    /**
     * 刷新令牌
     * @param refreshToken
     * @return
     */
    @PostMapping("/refresh-token")
    @ApiOperation(value = "刷新令牌")
    public Result<BaseTokenDTO> refreshToken(String refreshToken) {
        BaseTokenDTO baseTokenDTO = baseTokenService.refreshToken(refreshToken);
        return Result.ofSuccess(baseTokenDTO);
    }

    /**
     * 获取当前用户
     * @return
     */
    @GetMapping("/current-user-info")
    @ApiOperation("获取登录用户信息")
    // @PreAuthorize("hasRole('ADMIN')")
    public Result<BaseUserDTO> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Result.ofSuccess(BeanUtil.copyProperties(authentication, BaseUserDTO.class));
    }

    /**
     * 鉴权
     * @param uri 请求uri
     * @param method 请求方法
     * @return
     */
    @GetMapping("/authorize")
    @ApiOperation("判断是否允许访问")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uri", value = "请求uri", required = true),
            @ApiImplicitParam(name = "method", value = "请求方法", required = true)
    })
    public Result<BaseUserDTO> authorize(@NotBlank String uri, @NotBlank String method) {
        if (!HttpMethodConst.ALL_HTTP_METHOD.contains(method.toUpperCase())) {
            throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "参数错误",
                    String.format("参数method=%s不正确", method));
        }
        List<BaseWhitelistDTO> whitelistDTOS = baseWhitelistService.findAll();
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        /*
            判断是否是白名单
         */
        long count = whitelistDTOS.stream()
                .filter(f -> antPathMatcher.match(f.getPattern(), uri) && f.getMethods().contains(method))
                .count();
        if (count > 0) {
            return Result.ofSuccess(BeanUtil.copyProperties(BaseUserPO.createAnonymousUser(), BaseUserDTO.class));
        }

        /*
            TODO 不是白名单，就需要判断该请求需要什么角色的权限
         */

        return Result.ofSuccess(BeanUtil.copyProperties(SecurityContextHolder.getContext().getAuthentication(), BaseUserDTO.class));
    }
}
