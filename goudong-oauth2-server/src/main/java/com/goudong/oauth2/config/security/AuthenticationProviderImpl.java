package com.goudong.oauth2.config.security;

import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.commons.dto.oauth2.BaseRoleDTO;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.oauth2.po.BaseRolePO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.service.BaseAuthenticationLogService;
import com.goudong.oauth2.service.BaseMenuService;
import com.goudong.oauth2.service.BaseRoleService;
import com.goudong.oauth2.service.BaseUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
@RequiredArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private static final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * BCrypt格式的字符串
     * @see BCryptPasswordEncoder#BCRYPT_PATTERN
     */
    private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

    /**
     * 用户服务接口
     */
    private final BaseUserService baseUserService;

    /**
     * 菜单接口
     */
    private final BaseMenuService baseMenuService;

    /**
     * 角色服务层接口
     */
    private final BaseRoleService baseRoleService;
    /**
     * 认证日志服务层接口
     */
    private final BaseAuthenticationLogService baseAuthenticationLogService;


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
            throw ClientException.client(ClientExceptionEnum.BAD_REQUEST, "请输入正确的用户名和密码");
        }

        // 将用户账号设置到request中，再登录成功和失败处理器中记录认证日志
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().setAttribute("principal", username);

        // 根据用户名查询用户是否存在
        UserDetails userInfo = baseUserService.loadUserByUsername(username);

        // 用户不存在
        AssertUtil.isNotNull(userInfo, () -> new UsernameNotFoundException("用户不存在"));

        boolean passwordMatches = false;
        if (BCRYPT_PATTERN.matcher(password).matches()) {
            passwordMatches = Objects.equals(password, userInfo.getPassword());
        } else {
            // 使用 BCrypt 加密的方式进行匹配
            passwordMatches = BCRYPT_PASSWORD_ENCODER.matches(password, userInfo.getPassword());

        }
        AssertUtil.isTrue(passwordMatches, () -> new BadCredentialsException("用户密码错误"));
        AssertUtil.isTrue(userInfo.isEnabled(), () -> new DisabledException("用户未激活"));
        AssertUtil.isTrue(userInfo.isAccountNonLocked(), () -> new LockedException("用户以锁定"));
        AssertUtil.isTrue(userInfo.isAccountNonExpired(), () -> new AccountExpiredException("账户已过期"));

        // 验证通过，返回用户信息
        BaseUserPO baseUserPO = BeanUtil.copyProperties(userInfo, BaseUserPO.class);

        // no session 解决jpa的lazy
        baseUserPO.getRoles().stream().forEach(p->p.setMenus(null));

        // 查询菜单
        List<Long> roleIds = baseUserPO.getRoles().stream().map(BaseRolePO::getId).collect(Collectors.toList());
        List<BaseRoleDTO> roleDTOS = baseRoleService.listByIds(roleIds);
        Set<BaseMenuDTO> menuDTOS = roleDTOS
                .stream()
                .flatMap(m -> m.getMenus().stream()).collect(Collectors.toSet());

        baseUserPO.setPassword(null);
        baseUserPO.setMenus(menuDTOS);
        return baseUserPO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
