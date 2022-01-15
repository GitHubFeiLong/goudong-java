package com.goudong.oauth2.service.impl;

import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.repository.BaseUserRepository;
import com.goudong.oauth2.service.BaseUserService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 类描述：
 * 用户服务实现
 * @author msi
 * @version 1.0
 * @date 2022/1/8 20:14
 */
@Service
public class BaseUserServiceImpl implements BaseUserService {

    private final BaseUserRepository baseUserRepository;

    public BaseUserServiceImpl(BaseUserRepository baseUserRepository) {
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
        BaseUserPO byLogin = baseUserRepository.findByLogin(username);
        // // 让角色加载
        // byLogin.getRoles();
        return byLogin;
    }

}
