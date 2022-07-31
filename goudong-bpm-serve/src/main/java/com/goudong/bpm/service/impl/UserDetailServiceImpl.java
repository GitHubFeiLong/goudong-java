package com.goudong.bpm.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 类描述：
 *
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
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
