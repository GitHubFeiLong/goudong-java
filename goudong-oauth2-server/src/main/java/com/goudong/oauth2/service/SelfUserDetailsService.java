package com.goudong.oauth2.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.po.AuthorityUserPO;
import com.goudong.commons.pojo.SelfUserDetails;
import com.goudong.oauth2.entity.User;
import com.goudong.oauth2.mapper.SelfAuthorityUserMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类描述：
 * 根据用户名查询用户
 * @author msi
 * @version 1.0
 * @date 2021/8/29 19:33
 */
@Service
public class SelfUserDetailsService implements UserDetailsService {
    private List<User> userList;

    @Resource
    private SelfAuthorityUserMapper selfAuthorityUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SelfUserDetails userInfo = new SelfUserDetails();
        // 查询用户信息
        LambdaQueryWrapper<AuthorityUserPO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.nested(i->i.eq(AuthorityUserPO::getUsername, username)
                .or()
                .eq(AuthorityUserPO::getEmail, username)
                .or()
                .eq(AuthorityUserPO::getPhone, username)
        ).nested(i->i.isNull(AuthorityUserPO::getValidTime).or().lt(AuthorityUserPO::getValidTime, LocalDateTime.now()));


        AuthorityUserPO user = selfAuthorityUserMapper.selectOne(lambdaQueryWrapper);

        if (user != null) {
            userInfo.setUsername(user.getUsername());
            userInfo.setPassword(user.getPassword());
        } else {
            throw ClientException.clientException(ClientExceptionEnum.UNPROCESSABLE_ENTITY, "用户名或密码错误");
        }

        Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
        // 查询用户权限
        List<String> roles = selfAuthorityUserMapper.selectRoleNameByUserId(user.getId());
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

