package com.goudong.oauth2.service.impl;

import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.oauth2.po.BaseAppPO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.repository.BaseAppRepository;
import com.goudong.oauth2.repository.BaseUserRepository;
import com.goudong.oauth2.service.MyUserDetailsService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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

    @Resource
    private BaseAppRepository baseAppRepository;
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

        // 后台管理员，就需要查询应用表
        if (appId.longValue() == 1667779450730426368L) {
            Optional<BaseAppPO> appPO = baseAppRepository.findByAppName(username);
            if (appPO.isPresent()) {
                return baseUserRepository.findByUsername(username).orElseGet(() -> null);
            }
            return null;
        }

        BaseUserPO byLogin = baseUserRepository.findByLogin(appId, username);
        return byLogin;
    }
}
