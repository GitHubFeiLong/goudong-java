package com.goudong.oauth2.service.impl;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.commons.exception.oauth2.AccessTokenExpiredException;
import com.goudong.commons.exception.user.AccountExpiredException;
import com.goudong.commons.exception.user.UserException;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.core.TokenExpires;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.properties.TokenExpiresProperties;
import com.goudong.oauth2.repository.BaseUserRepository;
import com.goudong.oauth2.service.BaseTokenService;
import com.goudong.oauth2.service.BaseUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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

    /**
     * 令牌服务
     */
    private final BaseTokenService baseTokenService;

    /**
     * 令牌过期配置
     */
    private final TokenExpiresProperties tokenExpiresProperties;

    public BaseUserServiceImpl(BaseUserRepository baseUserRepository,
                               RedisTool redisTool,
                               BaseTokenService baseTokenService,
                               TokenExpiresProperties tokenExpiresProperties) {
        this.baseUserRepository = baseUserRepository;
        this.redisTool = redisTool;
        this.baseTokenService = baseTokenService;
        this.tokenExpiresProperties = tokenExpiresProperties;
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
     * 保存令牌和用户信息到redis中
     *
     * @param baseUserPO  用户信息
     * @param clientSideEnum  客户端类型
     * @param accessToken 访问令牌
     */
    @Override
    public void saveAccessToken2Redis(BaseUserPO baseUserPO, ClientSideEnum clientSideEnum, String accessToken) {
        // 删除多余的属性
        BaseUserDTO baseUserDTO = BeanUtil.copyProperties(baseUserPO, BaseUserDTO.class);
        // 设置key
        redisTool.set(RedisKeyProviderEnum.AUTHENTICATION, baseUserDTO, clientSideEnum.getLowerName(), accessToken);
        // 修改key过期时间
        TokenExpires tokenExpires = TokenExpires.getTokenExpires(clientSideEnum, tokenExpiresProperties);
        redisTool.expireByCustom(RedisKeyProviderEnum.AUTHENTICATION, tokenExpires.getAccess(),
                tokenExpires.getAccessTimeUnit(), clientSideEnum.getLowerName(), accessToken);
    }

    /**
     * 获取当前请求用户认证信息
     *
     * @param request
     * @return
     */
    @Override
    public BaseUserPO getAuthentication(HttpServletRequest request) {
        // 获取请求头中设置的accessToken
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(header) && header.startsWith("Bearer ")) {
            // 截取，获取完整的访问令牌
            String accessToken = header.substring(7);
            // 获取客户端类型
            ClientSideEnum clientSide = ClientSideEnum.getClientSide(request);
            String clientSideLowerName = clientSide.getLowerName();

            // 获取redis中用户信息
            String json = redisTool.getString(RedisKeyProviderEnum.AUTHENTICATION, clientSideLowerName, accessToken);
            if (StringUtils.isNotBlank(json)) {
                // 解析成对象
                BaseUserPO baseUserPO = JSON.parseObject(json, BaseUserPO.class);

                // 判断用户是否过期，未过期直接返回 TODO 后期有锁定等其它状态直接追加判断
                if (baseUserPO.isAccountNonExpired()) {
                    return baseUserPO;
                }

                throw new AccountExpiredException("账户已过期");
            }

            // redis中不存在了，从数据库加载用户，然后判断是否离线
            BaseTokenDTO tokenDTO = baseTokenService.findByAccessTokenAndClientType(accessToken, clientSideLowerName);
            if (tokenDTO != null) {
                // 判断访问令牌是否过期
                Date accessExpires = tokenDTO.getAccessExpires();
                if (accessExpires.after(new Date())) {
                    // 未过期，将其用户设置到redis中
                    BaseUserPO baseUserPO = baseUserRepository.findById(tokenDTO.getUserId())
                            .orElseThrow(() -> new UserException(ClientExceptionEnum.UNAUTHORIZED,
                                    "请重新登录", "令牌对应的用户id未查找到用户信息"));

                    // 保存到redis中
                    this.saveAccessToken2Redis(baseUserPO, clientSide, tokenDTO.getAccessToken());

                    // 返回用户信息
                    return baseUserPO;
                }

                // 访问令牌过期
                throw new AccessTokenExpiredException("访问令牌过期");
            }

        }

        // redis中不存在，数据库不存在令牌，设置一个匿名用户
        return BaseUserPO.createAnonymousUser();
    }

}
