package com.goudong.authentication.server.service.impl;

import com.goudong.core.util.StringUtil;
import com.goudong.authentication.server.constant.HttpHeaderConst;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.repository.BaseAppRepository;
import com.goudong.authentication.server.repository.BaseUserRepository;
import com.goudong.authentication.server.service.MyUserDetailsService;
import com.goudong.authentication.server.service.dto.MyUserDetails;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 登录：1. 查询用户
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
     * 加载根据用户名加载用户
     * 当认证失败
     * @see AuthenticationException
     * @param username
     * @return
     * @throws AuthenticationException
     */
    @Override
    @Deprecated
    public MyUserDetails loadUserByUsername(String username) throws AuthenticationException {
        // 登录下拉选的appId
        String selectAppIdStr = httpServletRequest.getParameter("appId");
        Long selectAppId = StringUtil.isNotBlank(selectAppIdStr) ? Long.parseLong(selectAppIdStr) : null;

        BaseUser user = null;
        if (selectAppId != null) {
            // 根据选择的appId查询用户
            user = baseUserRepository.findByLogin(selectAppId, username);
        }

        // 请求头上的appId
        Long xAppId =  (Long)httpServletRequest.getAttribute(HttpHeaderConst.X_APP_ID);
        // 选择查询的用户不存在
        user = user != null ? user: baseUserRepository.findByLogin(xAppId, username);

        if (user != null) {
            MyUserDetails myUserDetails = new MyUserDetails();
            myUserDetails.setId(user.getId());
            myUserDetails.setSelectAppId(selectAppId == null ? selectAppId : xAppId);
            myUserDetails.setAppId(user.getAppId());
            myUserDetails.setUsername(user.getUsername());
            myUserDetails.setPassword(user.getPassword());
            myUserDetails.setEnabled(user.getEnabled());
            myUserDetails.setLocked(user.getLocked());
            myUserDetails.setValidTime(user.getValidTime());
            return myUserDetails;
        }

        return null;
    }
}
