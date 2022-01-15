package com.goudong.oauth2.service;


import com.goudong.oauth2.repository.BaseUserRepository;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * 类描述：
 * 根据用户名查询用户
 * @author msi
 * @version 1.0
 * @date 2021/8/29 19:33
 */
@Service
public class BaseUserService implements UserDetailsService {

    private final BaseUserRepository baseUserRepository;

    public BaseUserService(BaseUserRepository baseUserRepository) {
        this.baseUserRepository = baseUserRepository;
    }

    /**
     * 加载根据用户名、手机号、邮箱 加载用户
     * 当认证失败
     * @see AuthenticationException
     * @param username
     * @return
     * @throws AuthenticationException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        return baseUserRepository.findByLogin(username);
    }
}

