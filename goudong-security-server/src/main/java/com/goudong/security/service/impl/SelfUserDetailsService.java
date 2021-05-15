package com.goudong.security.service.impl;

import com.goudong.commons.entity.SelfUserDetails;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.security.dao.SelfAuthorityUserDao;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andon
 * @date 2019/3/20
 * <p>
 * 自定义用户认证
 */
@Component
public class SelfUserDetailsService implements UserDetailsService {

    @Resource
    private SelfAuthorityUserDao selfAuthorityUserDao;

    /**
     * 根据用户登录名查询用户信息
     * @param username 用户名/手机号/邮箱
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) {

        SelfUserDetails userInfo = new SelfUserDetails();
        // 查询用户信息
        AuthorityUserPO user = selfAuthorityUserDao.selectUserByUsername(username);
        if (user != null) {
            userInfo.setUsername(user.getUsername());
            userInfo.setPassword(user.getPassword());
        } else {
            throw new BadCredentialsException("账户名与密码不匹配，请重新输入");
        }

        Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
        // 查询用户权限
        List<String> roles = selfAuthorityUserDao.selectRoleNameByUserUuid(user.getUuid());
        for (String roleName : roles) {
            //用户拥有的角色
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authoritiesSet.add(simpleGrantedAuthority);
        }
        // 设置用户的角色
        userInfo.setAuthorities(authoritiesSet);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userInfo;
    }
}
