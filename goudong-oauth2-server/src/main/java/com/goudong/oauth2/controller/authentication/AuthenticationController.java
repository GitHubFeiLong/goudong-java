package com.goudong.oauth2.controller.authentication;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.boot.web.validation.EnumValidator;
import com.goudong.commons.annotation.core.Inner;
import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.core.lang.Result;
import com.goudong.core.util.CollectionUtil;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.exception.Oauth2Exception;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.service.BaseMenuService;
import com.goudong.oauth2.service.BaseTokenService;
import com.goudong.oauth2.service.BaseUserService;
import com.goudong.oauth2.service.BaseWhitelistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
@RequiredArgsConstructor
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

    /**
     * 用户服务
     */
    private final BaseUserService baseUserService;

    /**
     * request对象
     */
    private final HttpServletRequest httpServletRequest;

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
    @Whitelist("退出登录接口")
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
    public Result<BaseUserDTO> authorize(@NotBlank String uri, @EnumValidator(enumClass = HttpMethod.class) String method) {
        // TODO 响应的用户需要进行精简，不然数据太大没必要网络带宽
        // 获取当前用户
        BaseUserPO authentication = (BaseUserPO)SecurityContextHolder.getContext().getAuthentication();

        // ADMIN用户直接不校验权限
        if (authentication.isAdmin()) {
            // 不需要鉴权，直接放行
            return Result.ofSuccess(authentication.copy());
        }

        /*
            判断是否是白名单
         */
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<BaseWhitelistDTO> whitelistDTOS = baseWhitelistService.findAll().stream()
                .filter(f -> antPathMatcher.match(f.getPattern(), uri) && f.getMethods().contains(method))
                .collect(Collectors.toList());
        // 请求是白名单或者是内部接口时（集合中所有元素的isInner都是true时）
        if (CollectionUtil.isNotEmpty(whitelistDTOS)) {
            // 是否是内部接口
            boolean isInner = whitelistDTOS.stream().filter(f -> f.getIsInner()).count() == whitelistDTOS.size();
            if (isInner) {
                // 内部接口，需要判断请求
                if (StringUtils.isNotBlank(httpServletRequest.getHeader(HttpHeaderConst.X_INNER))) {
                    LogUtil.debug(log, "本次请求属于内部请求,不需要鉴权，url:{}", httpServletRequest.getRequestURI());
                    return Result.ofSuccess(authentication.copy());
                }
                // 不是内部请求
                LogUtil.debug(log, "本次请求不属于内部请求，url:{}", httpServletRequest.getRequestURI());

            } else {
                LogUtil.debug(log, "本次请求命中白名单，不需要鉴权:{}", httpServletRequest.getRequestURI());
                // 白名单直接放行
                return Result.ofSuccess(authentication.copy());
            }
        }

        /*
            不是白名单，不是内部调用，就需要判断该请求需要什么角色的权限
         */
        List<BaseMenuDTO> allMenu = baseMenuService.findAll();

        // 判断是否需要鉴权
        Optional<BaseMenuDTO> menuOptional = allMenu.parallelStream()
                // 接口且是内链
                .filter(f -> f.getType() == 0 && f.getOpenModel() == 0)
                .filter(f -> antPathMatcher.match(f.getPath(), uri) && f.getMethod().toUpperCase().indexOf(method.toUpperCase()) != -1)
                .findFirst();

        boolean isNeedAuthentication = menuOptional.isPresent();

        // 只有需要”鉴权“时，才进行鉴权
        if (isNeedAuthentication) {
            // 如果用户此时是匿名用户，那么就直接拒绝访问
            if (authentication.getId() == 0) {
                LogUtil.warn(log, "拒绝匿名用户访问敏感资源，必须先认证");
                // 没有权限，拒绝访问
                throw new Oauth2Exception(ClientExceptionEnum.UNAUTHORIZED);
            }

            // 循环用户所有角色
            for (GrantedAuthority role : authentication.getAuthorities()) {
                // 查询角色拥有的权限
                List<BaseMenuDTO> menus = baseMenuService.findAllByRole(role.getAuthority());
                // 循环权限，查看是否符合
                for (BaseMenuDTO menu : menus) {
                    String menuUrl = menu.getPath();
                    String menuMethod = menu.getMethod();
                    // 符合条件，退出循环
                    if (antPathMatcher.match(menuUrl, uri) && menuMethod.toUpperCase().indexOf(method.toUpperCase()) !=-1) {
                        LogUtil.debug(log, "本次请求用户有权访问role:{} uri:{}", role.getAuthority(), httpServletRequest.getRequestURI());
                        return Result.ofSuccess(authentication.copy());
                    }
                }
            }

            // 没有权限，拒绝访问
            throw new Oauth2Exception(ClientExceptionEnum.FORBIDDEN);
        }

        // 不需要鉴权，直接放行
        return Result.ofSuccess(authentication.copy());
    }

    /**
     * 通过用户名获取用户基本信息：用户角色权限，其余敏感信息不予返回
     * @param username 用户名
     * @return
     */
    @Inner(value = "loadUserByUsername", disable = true)
    @GetMapping("/loadUserByUsername")
    @ApiOperation("根据用户名获取用户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
    })
    public Result<BaseUserDTO> loadUserByUsername(@NotBlank String username) {
        BaseUserPO userDetails = (BaseUserPO)baseUserService.loadUserByUsername(username);
        // 只保留基本信息
        BaseUserPO incomplete = new BaseUserPO();
        incomplete.setUsername(userDetails.getUsername());
        incomplete.setRoles(userDetails.getRoles());
        return Result.ofSuccess(BeanUtil.copyProperties(incomplete, BaseUserDTO.class));
    }
}
