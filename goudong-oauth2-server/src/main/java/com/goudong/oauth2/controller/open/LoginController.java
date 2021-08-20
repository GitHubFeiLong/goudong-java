package com.goudong.oauth2.controller.open;

import com.goudong.commons.annotation.IgnoreResource;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.AuthorityUserUtil;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.vo.AuthorityUserVO;
import com.goudong.oauth2.mapper.AuthorityUserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 * 登录和退出控制器（只是接口展示方便swagger上查看文档）
 * @Author msi
 * @Date 2021-05-02 13:33
 * @Version 1.0
 */
@Api(tags = "登录")
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class LoginController {

    private AuthorityUserMapper authorityUserMapper;

    @Autowired
    public void setAuthorityUserMapper(AuthorityUserMapper authorityUserMapper) {
        this.authorityUserMapper = authorityUserMapper;
    }

    private AuthorityUserUtil authorityUserUtil;

    @Autowired
    public void setAuthorityUserUtil(AuthorityUserUtil authorityUserUtil) {
        this.authorityUserUtil = authorityUserUtil;
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
    })
    @IgnoreResource("登录")
    public Result<AuthorityUserVO> login (String username, String password, HttpServletResponse response) {
        // 查询用户信息
        AuthorityUserDTO authorityUserDTO = authorityUserMapper.selectUserDetailByUsername(username);

        String token = JwtTokenUtil.generateToken(authorityUserDTO, JwtTokenUtil.VALID_HOUR);

        // 转成VO
        AuthorityUserVO authorityUserVO = BeanUtil.copyProperties(authorityUserDTO, AuthorityUserVO.class);
        // 设置到响应头里
        response.setHeader(JwtTokenUtil.TOKEN_HEADER, token);

        // 将用户登录信息保存到redis中
        authorityUserUtil.login(token, authorityUserDTO);

        return Result.ofSuccess(authorityUserVO);
    }

    @PutMapping("/logout")
    @ApiOperation(value = "注销")
    @IgnoreResource("注销")
    public Result<String> logout (HttpServletRequest request) {
        // 获取token
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);

        AuthorityUserDTO authorityUserDTO = authorityUserUtil.getUserDetails();
        if (token != null) {
            authorityUserUtil.logout(token, authorityUserDTO.getId());
        }

        return Result.ofSuccess("退出成功");
    }

}
