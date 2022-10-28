package com.goudong.bpm.service.impl;

import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.utils.core.AssertUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 类描述：
 * UserDetailService
 * 因为activiti内部有使用spring security的UserDetailsService#loadUserByUsername(String username)
 * 所以我们需要定义下。
 * @author cfl
 * @version 1.0
 * @date 2022/7/31 23:35
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 根据用户名获取用户详情
     *
     * @param username the username identifying the user whose data is required.
     *
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 空校验
        AssertUtil.notNull(authentication, ClientExceptionEnum.FORBIDDEN);
        // 将当前登录用户返回调用方
        User user = new User(authentication.getName(), String.valueOf(authentication.getCredentials()), authentication.getAuthorities());
        return user;
    }
}
