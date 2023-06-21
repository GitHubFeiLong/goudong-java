package com.goudong.oauth2.service.impl;

import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.repository.BaseUserRepository;
import com.goudong.oauth2.service.MyUserDetailsService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/6/21 10:02
 */
@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsService {

    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private BaseUserRepository baseUserRepository;
    /**
     * 加载根据用户名、手机号、邮箱 加载用户
     * 当认证失败
     * @see AuthenticationException
     * @param username
     * @return
     * @throws AuthenticationException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        Long appId = (Long)httpServletRequest.getAttribute(HttpHeaderConst.X_APP_ID);
        BaseUserPO byLogin = baseUserRepository.findByLogin(appId, username);
        return byLogin;
    }
}
