package com.goudong.oauth2.config;

import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.oauth2.service.BaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义登录认证
 * @Author msi
 * @Date 2021-04-02 13:14
 * @Version 1.0
 */
@Slf4j
@Component
public class SelfAuthenticationProvider implements AuthenticationProvider {

    private static final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final BaseUserService baseUserService;

    public SelfAuthenticationProvider(BaseUserService baseUserService) {
        this.baseUserService = baseUserService;
    }

    /**
     * 自定义登录认证
     *
     * @param authentication
     * @throws UsernameNotFoundException
     * @throws AccountExpiredException
     * @throws BadCredentialsException
     * @return
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        //表单输入的用户名
        String username = (String) authentication.getPrincipal();
        //表单输入的密码
        String password = (String) authentication.getCredentials();

        // 用户名/电话/邮箱不传时直接抛出异常
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "请输入正确的用户名和密码");
        }
        // 根据用户名查询用户是否存在
        UserDetails userInfo = baseUserService.loadUserByUsername(username);

        // 用户不存在
        if (userInfo == null) {
            throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "请输入正确的用户名和密码");
            // throw new UsernameNotFoundException("用户不存在");
        }

        // 使用 BCrypt 加密的方式进行匹配
        boolean matches = BCRYPT_PASSWORD_ENCODER.matches(password, userInfo.getPassword());

        // 密码不正确，抛出异常
        if (!matches) {
            throw new BadCredentialsException("用户密码错误");
        }

        // 账户已过期
        if (!userInfo.isAccountNonExpired()) {
            throw new AccountExpiredException("账户已过期");
        }

        // 验证通过，返回用户信息
        return new UsernamePasswordAuthenticationToken(username, userInfo.getPassword(), userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
