package com.goudong.oauth2.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.goudong.commons.constant.user.RoleConst;
import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.commons.exception.user.AccountExpiredException;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.oauth2.core.AuthenticationImpl;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.po.BaseRolePO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.repository.BaseUserRepository;
import com.goudong.oauth2.service.BaseUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 用户服务实现
 * @author msi
 * @version 1.0
 * @date 2022/1/8 20:14
 */
@Service
public class BaseUserServiceImpl implements BaseUserService {

    /**
     * 用户持久层
     */
    private final BaseUserRepository baseUserRepository;

    /**
     * redis操作对象
     */
    private final RedisTool redisTool;

    public BaseUserServiceImpl(BaseUserRepository baseUserRepository, RedisTool redisTool) {
        this.baseUserRepository = baseUserRepository;
        this.redisTool = redisTool;
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
        return byLogin;
    }

    /**
     * 获取当前请求用户认证信息
     *
     * @param request
     * @return
     */
    @Override
    public AuthenticationImpl getAuthentication(HttpServletRequest request) throws JsonProcessingException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(header) && header.startsWith("Bearer ")) {
            // 获取完整的访问令牌
            String accessToken = header.substring(7);
            // 获取客户端类型
            ClientSideEnum clientSide = ClientSideEnum.getClientSide(request);
            String clientSideLowerName = clientSide.getLowerName();

            // 获取redis中用户信息
            String json = redisTool.getString(RedisKeyProviderEnum.AUTHENTICATION, clientSideLowerName, accessToken);
            if (StringUtils.isNotBlank(json)) {
                AuthenticationImpl authentication = JSON.parseObject(json, AuthenticationImpl.class);

                // 判断用户是否过期
                boolean accountNonExpired = authentication.isAccountNonExpired();

                if (accountNonExpired) {
                    return authentication;
                }

                throw new AccountExpiredException("账户已过期");
            }
        }

        // 匿名用户
        AuthenticationImpl authentication = new AuthenticationImpl();
        authentication.setAuthenticated(true);
        authentication.setId(0L);
        authentication.setUsername("匿名用户");
        // 创建匿名角色
        BaseRolePO baseRolePO = new BaseRolePO();
        baseRolePO.setId(0L);
        baseRolePO.setRoleName(RoleConst.ROLE_ANONYMOUS);
        baseRolePO.setRoleNameCn("匿名角色");
        authentication.setRoles(Lists.newArrayList(baseRolePO));


        return authentication;
    }

}
