package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.common.core.Jwt;
import com.goudong.authentication.common.core.LoginResp;
import com.goudong.authentication.common.core.Token;
import com.goudong.authentication.common.core.UserSimple;
import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.RefreshToken;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 用户管理服务层接口实现类
 * @author chenf
 * @version 1.0
 */
@Slf4j
@Service
public class BaseUserManagerServiceImpl implements BaseUserManagerService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseUserService baseUserService;

    @Resource
    private BaseAppService baseAppService;

    //~methods
    //==================================================================================================================
    /**
     * 根据应用Id和用户名查询用户
     *
     * @param appId    应用Id
     * @param username 用户名
     * @return 返回用户
     */
    @Override
    public BaseUser findOneByAppIdAndUsername(Long appId, String username) {
        BaseUser baseUser = baseUserService.findOneByAppIdAndUsername(appId, username);
        return baseUser;
    }

    /**
     * 获取登录成功信息
     * @param myAuthentication 用户认证成功对象
     * @return 用户基本信息和token
     */
    @Override
    public LoginResp login(MyAuthentication myAuthentication) {
        log.info("认证成功，响应用户登录信息:{}", myAuthentication);
        LoginResp loginResp = new LoginResp();
        // 设置角色
        List<String> roles = myAuthentication.getRoles().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        loginResp.setId(myAuthentication.getId());
        loginResp.setUsername(myAuthentication.getUsername());
        loginResp.setAppId(myAuthentication.getAppId());
        loginResp.setRealAppId(myAuthentication.getRealAppId());
        loginResp.setRoles(roles);

        BaseApp app = baseAppService.findById(myAuthentication.getAppId());

        // 创建token
        Jwt jwt = new Jwt(1, TimeUnit.DAYS, app.getSecret());
        UserSimple userSimple = new UserSimple(myAuthentication.getId(), myAuthentication.getAppId(), myAuthentication.getRealAppId(), myAuthentication.getUsername(), roles);
        Token token = jwt.generateToken(userSimple);
        loginResp.setToken(token);
        // 设置应用首页地址
        loginResp.setHomePage(app.getHomePage());

        return loginResp;
    }

    /**
     * 刷新token
     *
     * @param token refreshToken
     * @return token
     */
    @Override
    public Token refreshToken(RefreshToken token) {
        return baseUserService.refreshToken(token);
    }
}
