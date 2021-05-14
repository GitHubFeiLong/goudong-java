package com.goudong.security.config;

import com.goudong.security.service.impl.SelfUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 自定义登录认证
 * @Author msi
 * @Date 2021-04-02 13:14
 * @Version 1.0
 */
@Slf4j
@Component
public class SelfAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private SelfUserDetailsService selfUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) {

        log.info("authentication >> {}", authentication.toString());
        // 登录请求中的其他参数
        CustomWebAuthenticationDetails customWebAuthenticationDetails = (CustomWebAuthenticationDetails) authentication.getDetails(); //获取身份验证详细信息
        String phone = customWebAuthenticationDetails.getPhone();
        String email = customWebAuthenticationDetails.getEmail();

        String username = (String) authentication.getPrincipal(); //表单输入的用户名
        String password = (String) authentication.getCredentials(); //表单输入的密码

        // 用户名/电话/邮箱不传时直接抛出异常
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("登录信息错误");
        }
        // 根据用户名查询用户是否存在
        UserDetails userInfo = selfUserDetailsService.loadUserByUsername(username);

        // 使用 BCrypt 加密的方式进行匹配
        boolean matches = new BCryptPasswordEncoder().matches(password, userInfo.getPassword()); //校验用户名密码
        // 密码不正确，抛出异常
        if (!matches) {
            throw new BadCredentialsException("密码错误");
        }
        // 验证通过，返回用户信息
        return new UsernamePasswordAuthenticationToken(username, userInfo.getPassword(), userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
