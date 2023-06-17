package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.core.ServerException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.oauth2.core.TokenExpires;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.dto.authentication.BaseRoleDTO;
import com.goudong.oauth2.dto.authentication.BaseUserDTO;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.exception.AccessTokenExpiredException;
import com.goudong.oauth2.exception.AccessTokenInvalidException;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.properties.TokenExpiresProperties;
import com.goudong.oauth2.repository.BaseAppRepository;
import com.goudong.oauth2.repository.BaseUserRepository;
import com.goudong.oauth2.service.BaseTokenService;
import com.goudong.oauth2.service.BaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private final ObjectMapper objectMapper;

    private final BaseAppRepository baseAppRepository;

    public BaseUserServiceImpl(BaseUserRepository baseUserRepository,
                               RedisTool redisTool,
                               @Lazy BaseTokenService baseTokenService,
                               TokenExpiresProperties tokenExpiresProperties,
                               HttpServletRequest httpServletRequest,
                               ObjectMapper objectMapper,
                               BaseAppRepository baseAppRepository) {
        this.baseUserRepository = baseUserRepository;
        this.redisTool = redisTool;
        this.baseTokenService = baseTokenService;
        this.tokenExpiresProperties = tokenExpiresProperties;
        this.httpServletRequest = httpServletRequest;
        this.objectMapper = objectMapper;
        this.baseAppRepository = baseAppRepository;
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
        Long appId = (Long)httpServletRequest.getAttribute(HttpHeaderConst.X_APP_ID);
        BaseUserPO byLogin = baseUserRepository.findByLogin(appId, username);
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

        /*
            创建对象，保存到redis中
         */
        BaseUserDTO dto = new BaseUserDTO();
        dto.setId(baseUserPO.getId());
        dto.setAppId(baseUserPO.getAppId());
        dto.setUsername(baseUserPO.getUsername());
        dto.setEmail(baseUserPO.getEmail());
        dto.setPhone(baseUserPO.getPhone());
        dto.setNickname(baseUserPO.getNickname());
        List<BaseRoleDTO> roleDTOList = new ArrayList<>(baseUserPO.getRoles().size());
        dto.setRoles(roleDTOList);
        baseUserPO.getRoles().stream().forEach(p -> {
            roleDTOList.add(new BaseRoleDTO(p.getId(), p.getRoleName(), p.getRoleNameCn()));
        });

        // 设置到缓存
        String key = RedisKeyProviderEnum.AUTHENTICATION.getFullKey(dto.getAppId(),
                clientSideEnum.getLowerName(),
                accessToken);
        try {
            String value = objectMapper.writeValueAsString(dto);
            TokenExpires tokenExpires = TokenExpires.getTokenExpires(clientSideEnum, tokenExpiresProperties);
            redisTool.opsForValue().set(key, value, tokenExpires.getAccess(), tokenExpires.getAccessTimeUnit());
        } catch (JsonProcessingException e) {
            log.error("对象转换json字符串异常：{}", e.getMessage());
            throw new ServerException(e);
        }
    }

    /**
     * 获取当前请求用户认证信息
     *
     * @param request
     * @return
     */
    @Override
    public BaseUserPO getAuthentication(HttpServletRequest request) {
        Long appId = (Long)request.getAttribute(HttpHeaderConst.X_APP_ID);
        LogUtil.info(log, "appId:{}, uri:{},method:{}", appId, request.getRequestURI(), request.getMethod());
        // 获取请求头中设置的accessToken
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorization) && authorization.startsWith("Bearer ")) {
            // 截取，获取完整的访问令牌
            String accessToken = authorization.substring(7);
            // 获取客户端类型
            ClientSideEnum clientSide = ClientSideEnum.getClientSide(request);
            String clientSideLowerName = clientSide.getLowerName();

            // 获取redis中用户信息
            String json = redisTool.getString(RedisKeyProviderEnum.AUTHENTICATION,
                    appId,
                    clientSideLowerName,
                    accessToken);
            if (StringUtils.isNotBlank(json)) {
                // 解析成对象
                BaseUserPO baseUserPO = null;
                try {
                    baseUserPO = objectMapper.readValue(json, BaseUserPO.class);
                    LogUtil.info(log, "user:{}", baseUserPO);
                    // 刷新redis中认证信息的失效时间
                    TokenExpires tokenExpires = TokenExpires.getTokenExpires(clientSide, tokenExpiresProperties);
                    redisTool.expireByCustom(RedisKeyProviderEnum.AUTHENTICATION,
                            tokenExpires.getAccess(),
                            tokenExpires.getAccessTimeUnit(),
                            appId,
                            clientSide.getLowerName(),
                            accessToken);

                    baseUserPO.setSessionId(accessToken);
                    return baseUserPO;
                } catch (JsonProcessingException e) {
                    throw new ServerException(e);
                }
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

                    AssertUtil.isTrue(baseUserPO.isEnabled(), () -> new DisabledException("用户未激活"));
                    AssertUtil.isTrue(baseUserPO.isAccountNonLocked(), () -> new LockedException("用户已锁定"));
                    AssertUtil.isTrue(baseUserPO.isAccountNonExpired(), () -> new AccountExpiredException("账户已过期"));

                    // 保存到redis中
                    this.saveAccessToken2Redis(baseUserPO, tokenDTO.getAccessToken());

                    // 返回用户信息
                    baseUserPO.setSessionId(tokenDTO.getAccessToken());
                    return baseUserPO;
                }

                // 访问令牌过期
                throw new AccessTokenExpiredException("访问令牌过期");
            }

            // 携带token，但是token无效。
            throw new AccessTokenInvalidException("访问令牌无效");
        }
        // 请求的sessionId，默认使用Cookie，当authorization不为空时，使用token做sessionId
        String sessionId = request.getHeader(HttpHeaders.COOKIE);
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
        // BaseUserPO byQqOpenId = baseUserRepository.findByQqOpenId(openId);
        return BeanUtil.copyProperties(null, BaseUserDTO.class);
    }


}
