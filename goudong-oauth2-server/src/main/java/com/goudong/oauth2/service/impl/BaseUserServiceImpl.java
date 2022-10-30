package com.goudong.oauth2.service.impl;

import com.alibaba.fastjson.JSON;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.commons.framework.redis.RedisTool;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.oauth2.core.TokenExpires;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.exception.AccessTokenExpiredException;
import com.goudong.oauth2.exception.AccessTokenInvalidException;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.properties.TokenExpiresProperties;
import com.goudong.oauth2.repository.BaseUserRepository;
import com.goudong.oauth2.service.BaseTokenService;
import com.goudong.oauth2.service.BaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 类描述：
 * 用户服务实现
 * @author msi
 * @version 1.0
 * @date 2022/1/8 20:14
 */
@Slf4j
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

    /**
     * 请求对象
     */
    private final HttpServletRequest httpServletRequest;

    public BaseUserServiceImpl(BaseUserRepository baseUserRepository,
                               RedisTool redisTool,
                               @Lazy BaseTokenService baseTokenService,
                               TokenExpiresProperties tokenExpiresProperties, HttpServletRequest httpServletRequest) {
        this.baseUserRepository = baseUserRepository;
        this.redisTool = redisTool;
        this.baseTokenService = baseTokenService;
        this.tokenExpiresProperties = tokenExpiresProperties;
        this.httpServletRequest = httpServletRequest;
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
    @Transactional
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        BaseUserPO byLogin = baseUserRepository.findByLogin(username);
        return byLogin;
    }


    /**
     * 保存令牌和用户信息到redis中
     *
     * @param baseUserPO  用户信息
     * @param accessToken 访问令牌
     */
    @Override
    public void saveAccessToken2Redis(BaseUserPO baseUserPO, String accessToken) {
        ClientSideEnum clientSideEnum = ClientSideEnum.getClientSide(httpServletRequest);

        // 删除多余的属性
        BaseUserDTO baseUserDTO = BeanUtil.copyProperties(baseUserPO, BaseUserDTO.class);
        baseUserDTO.getRoles().stream().forEach(p->p.setMenus(null));
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
        LogUtil.info(log, "uri:{},method:{}", request.getRequestURI(), request.getMethod());
        // 获取请求头中设置的accessToken
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 请求的sessionId，默认使用Cookie，当authorization不为空时，使用token做sessionId
        String sessionId = request.getHeader(HttpHeaders.COOKIE);
        if (StringUtils.isNotBlank(authorization) && authorization.startsWith("Bearer ")) {
            // 截取，获取完整的访问令牌
            String accessToken = authorization.substring(7);
            // 获取客户端类型
            ClientSideEnum clientSide = ClientSideEnum.getClientSide(request);
            String clientSideLowerName = clientSide.getLowerName();

            // 有token，就将session设置成token
            sessionId = accessToken;

            // 获取redis中用户信息
            String json = redisTool.getString(RedisKeyProviderEnum.AUTHENTICATION, clientSideLowerName, accessToken);
            if (StringUtils.isNotBlank(json)) {
                // 解析成对象
                BaseUserPO baseUserPO = JSON.parseObject(json, BaseUserPO.class);

                // 判断用户是否过期，未过期直接返回 TODO 后期有锁定等其它状态直接追加判断
                if (baseUserPO.isAccountNonExpired()) {
                    LogUtil.info(log, "user:{}", baseUserPO);
                    baseUserPO.setSessionId(sessionId);

                    // 刷新redis中认证信息的失效时间
                    TokenExpires tokenExpires = TokenExpires.getTokenExpires(clientSide, tokenExpiresProperties);
                    redisTool.expireByCustom(RedisKeyProviderEnum.AUTHENTICATION, tokenExpires.getAccess(),
                            tokenExpires.getAccessTimeUnit(), clientSide.getLowerName(), accessToken);

                    return baseUserPO;
                }

                throw new AccountExpiredException("账户已过期");
            }

            // 用户不活跃，导致Redis key的过期，需要从数据库中加载用户，再判断是否有效（通常是手机端）
            BaseTokenDTO tokenDTO = baseTokenService.findByAccessTokenAndClientType(accessToken, clientSideLowerName);
            if (tokenDTO != null) {
                // 判断访问令牌是否过期
                Date accessExpires = tokenDTO.getAccessExpires();
                if (accessExpires.after(new Date())) {
                    // 未过期，将其用户设置到redis中
                    BaseUserPO baseUserPO = baseUserRepository.findById(tokenDTO.getUserId())
                            .orElseThrow(() -> ClientException.client(ClientExceptionEnum.UNAUTHORIZED,
                                    "请重新登录", "令牌对应的用户id未查找到用户信息"));

                    // 账户是否未过期
                    if (baseUserPO.isAccountNonExpired()) {
                        // 保存到redis中
                        this.saveAccessToken2Redis(baseUserPO, tokenDTO.getAccessToken());

                        // 返回用户信息
                        baseUserPO.setSessionId(sessionId);
                        return baseUserPO;
                    }

                    // 账户已过期
                    throw new AccountExpiredException("账户已过期");
                }

                // 访问令牌过期
                throw new AccessTokenExpiredException("访问令牌过期");
            }

            // 携带token，但是token无效。
            throw new AccessTokenInvalidException("访问令牌无效");
        }

        // redis中不存在，数据库不存在令牌，设置一个匿名用户
        return BaseUserPO.createAnonymousUser(sessionId);
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public BaseUserDTO findById(Long userId) {
        return BeanUtil.copyProperties(baseUserRepository.findById(userId).orElse(null), BaseUserDTO.class);
    }

    /**
     * 根据openId查询用户信息
     *
     * @param openId
     * @return
     */
    @Override
    public BaseUserDTO findByOpenId(String openId) {
        BaseUserPO byQqOpenId = baseUserRepository.findByQqOpenId(openId);
        return BeanUtil.copyProperties(byQqOpenId, BaseUserDTO.class);
    }


}
