package com.goudong.oauth2.config.security;

import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.service.BaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 类描述：
 * 自定义认证处理
 *
 * @author msi
 * @date 2022/1/15 20:26
 * @version 1.0
 */
@Slf4j
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private static final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * BCrypt格式的字符串
     * @see BCryptPasswordEncoder#BCRYPT_PATTERN
     */
    private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");
    private final BaseUserService baseUserService;

    public AuthenticationProviderImpl(BaseUserService baseUserService) {
        this.baseUserService = baseUserService;
    }

    /**
     * 自定义登录认证
     *
     * @param authentication 前端传递的认证参数，包含用户名密码
     * @throws UsernameNotFoundException
     * @throws AccountExpiredException
     * @throws BadCredentialsException
     * @return 认证成功对象
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
            throw new UsernameNotFoundException("用户不存在");
        }

        boolean passwordMatches = false;
        if (BCRYPT_PATTERN.matcher(password).matches()) {
            passwordMatches = Objects.equals(password, userInfo.getPassword());
        } else {
            // 使用 BCrypt 加密的方式进行匹配
            passwordMatches = BCRYPT_PASSWORD_ENCODER.matches(password, userInfo.getPassword());

        }
        // 密码不正确，抛出异常
        if (!passwordMatches) {
            throw new BadCredentialsException("用户密码错误");
        }

        // 账户已过期
        if (!userInfo.isAccountNonExpired()) {
            throw new AccountExpiredException("账户已过期");
        }

        // 验证通过，返回用户信息
        BaseUserPO baseUserPO = BeanUtil.copyProperties(userInfo, BaseUserPO.class);
        baseUserPO.setPassword(null);
        return baseUserPO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
